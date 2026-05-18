package com.tansen.app.vote.core;

import com.tansen.app.util.redisutil.VoteRedisKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final long VOTE_TTL_DAYS = 7;

    /**
     * Cast a vote. Returns true if successful, false if user already voted.
     */
    public boolean castVote(Long complaintId, Long userId) {
        String voterKey = VoteRedisKeys.voterSet(complaintId);
        String countKey = VoteRedisKeys.voteCount(complaintId);

        //  returns 1 if added, 0 if already exists — atomic in Redis
        boolean added = redisTemplate.opsForSet().add(voterKey, userId.toString()) == 1;

        if (!added) {
            return false; // Already voted
        }

        // Increment counter
        redisTemplate.opsForValue().increment(countKey);

        // Set TTL so stale data auto-expires
        redisTemplate.expire(voterKey, Duration.ofDays(VOTE_TTL_DAYS));
        redisTemplate.expire(countKey, Duration.ofDays(VOTE_TTL_DAYS));

        // Mark complaint as dirty (needs DB sync)
        redisTemplate.opsForSet().add(VoteRedisKeys.DIRTY_COMPLAINTS, complaintId.toString());

        log.debug("Vote cast: complaintId={}, userId={}", complaintId, userId);
        return true;
    }

    /**
     * Remove a vote (unvote). Returns true if successful.
     */
    public boolean removeVote(Long complaintId, Long userId) {
        String voterKey = VoteRedisKeys.voterSet(complaintId);
        String countKey = VoteRedisKeys.voteCount(complaintId);

        Long removed = redisTemplate.opsForSet().remove(voterKey, userId.toString());

        if (removed == null || removed == 0) {
            return false; // Never voted
        }

        redisTemplate.opsForValue().decrement(countKey);
        redisTemplate.opsForSet().add(VoteRedisKeys.DIRTY_COMPLAINTS, complaintId.toString());

        return true;
    }

    /**
     * Check if a user has voted on a complaint.
     */
    public boolean hasVoted(Long complaintId, Long userId) {
        String voterKey = VoteRedisKeys.voterSet(complaintId);
        return Boolean.TRUE.equals(
                redisTemplate.opsForSet().isMember(voterKey, userId.toString())
        );
    }

    /**
     * Get vote count from Redis.
     */
    public long     getVoteCount(Long complaintId) {
        String countKey = VoteRedisKeys.voteCount(complaintId);
        Object value = redisTemplate.opsForValue().get(countKey);
        if (value == null) return 0L;
        return Long.parseLong(value.toString());
    }

    /**
     * Get all voter IDs for a complaint.
     */
    public Set<String> getVoterIds(Long complaintId) {
        String voterKey = VoteRedisKeys.voterSet(complaintId);
        Set<Object> members = redisTemplate.opsForSet().members(voterKey);
        if (members == null) return Collections.emptySet();
        return members.stream().map(Object::toString).collect(Collectors.toSet());
    }

    /**
     * Get all complaint IDs that have pending unsynced votes.
     */
    public Set<String> getDirtyComplaintIds() {
        Set<Object> members = redisTemplate.opsForSet().members(VoteRedisKeys.DIRTY_COMPLAINTS);
        if (members == null) return Collections.emptySet();
        return members.stream().map(Object::toString).collect(Collectors.toSet());
    }

    /**
     * Mark a complaint as synced (remove from dirty set).
     */
    public void markAsSynced(Long complaintId) {
        redisTemplate.opsForSet().remove(VoteRedisKeys.DIRTY_COMPLAINTS, complaintId.toString());
    }

    /**
     * Seed Redis from DB on startup (warm-up).
     */
    public void seedFromDatabase(Long complaintId, long dbCount, Set<String> voterIds) {
        String countKey = VoteRedisKeys.voteCount(complaintId);
        String voterKey = VoteRedisKeys.voterSet(complaintId);

        // Only seed if Redis has no data for this complaint
        if (Boolean.FALSE.equals(redisTemplate.hasKey(countKey))) {
            redisTemplate.opsForValue().set(countKey, String.valueOf(dbCount));
            if (!voterIds.isEmpty()) {
                redisTemplate.opsForSet().add(voterKey, voterIds.toArray());
            }
            redisTemplate.expire(countKey, Duration.ofDays(VOTE_TTL_DAYS));
            redisTemplate.expire(voterKey, Duration.ofDays(VOTE_TTL_DAYS));
        }
    }
}