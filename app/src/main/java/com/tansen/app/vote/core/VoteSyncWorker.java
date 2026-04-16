package com.tansen.app.vote.core;

import com.tansen.entity.Complaint;
import com.tansen.entity.User;
import com.tansen.entity.Vote;
import com.tansen.repository.ComplaintRepository;
import com.tansen.repository.UserRepository;
import com.tansen.repository.VoteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class VoteSyncWorker {

    private final VoteRedisService voteRedisService;
    private final VoteRepository voteRepository;
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    /**
     * Runs every 30 seconds — syncs dirty complaints from Redis to SQL.
     */
    @Scheduled(fixedDelay = 30_000)
    @Transactional
    public void syncVotesToDatabase() {
        Set<String> dirtyIds = voteRedisService.getDirtyComplaintIds();

        if (dirtyIds.isEmpty()) {
            return;
        }

        log.info("VoteSyncWorker: syncing {} complaint(s)", dirtyIds.size());

        for (String complaintIdStr : dirtyIds) {
            try {
                Long complaintId = Long.parseLong(complaintIdStr);
                syncComplaint(complaintId);
                voteRedisService.markAsSynced(complaintId);
            } catch (Exception e) {
                log.error("Failed to sync votes for complaintId={}", complaintIdStr, e);
                // Leave it dirty — will retry on next cycle
            }
        }
    }

    private void syncComplaint(Long complaintId) {
        Set<String> redisVoterIds = voteRedisService.getVoterIds(complaintId);


        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new EntityNotFoundException("Complaint not found: " + complaintId));

        for (String userIdStr : redisVoterIds) {
            Long userId = Long.parseLong(userIdStr);

            // Skip if already persisted
            if (voteRepository.existsByComplaintIdAndUserId(complaintId, userId)) {
                continue;
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

            Vote vote = new Vote();
            vote.setComplaint(complaint);
            vote.setUser(user);
            vote.setRemarks("N/A - recovered from Redis");  // remarks unavailable in Redis
            vote.setVotedAt(LocalDateTime.now());
            vote.setSyncedFromRedis(true);

            voteRepository.save(vote);
        }

        log.debug("Synced {} voters for complaintId={}", redisVoterIds.size(), complaintId);
    }
}
