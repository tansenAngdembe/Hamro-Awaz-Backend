package com.tansen.app.vote.service.impl;

import com.tansen.app.vote.core.VoteRedisService;
import com.tansen.app.vote.dto.CastVoteRequest;
import com.tansen.app.vote.dto.ComplaintIdRequest;
import com.tansen.app.vote.dto.VoteResponseDto;
import com.tansen.app.vote.service.VoteService;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.Complaint;
import com.tansen.entity.User;
import com.tansen.entity.Vote;
import com.tansen.repository.ComplaintRepository;
import com.tansen.repository.UserRepository;
import com.tansen.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteServiceImpl implements VoteService {
    private static final Logger LOG = LoggerFactory.getLogger(VoteServiceImpl.class);

    private final VoteRedisService voteRedisService;
    private final VoteRepository voteRepository;
//    private final VoteMapper voteMapper;
    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    // ── Cast Vote
    @Override
    public ApiResponse<?> castVote(CastVoteRequest castVoteRequest, Principal loggedInUser) {
        User user = userRepository.findByEmail(loggedInUser.getName());
        if(user == null) {
            return ResponseUtil.getSuccessfulApiResponse("User not found with email: " + loggedInUser.getName());
        }

        Long userId = user.getId();
        Complaint complaint = complaintRepository.findByUniqueId(castVoteRequest.getComplaintId());
        if(complaint == null) {
            return ResponseUtil.getSuccessfulApiResponse("Complaint not found");
        }
        Long complaintId = complaint.getId();

        if (voteRedisService.hasVoted(complaintId, userId)
                || voteRepository.existsByComplaintIdAndUserId(complaintId, userId)) {
            return ResponseUtil.getSuccessfulApiResponse("You have already voted on this complaint");
//            throw new AlreadyVotedException("You have already voted on this complaint.");
        }

        boolean success = voteRedisService.castVote(complaintId, userId);
        if (!success) {
            return ResponseUtil.getSuccessfulApiResponse("You have already voted on this complaint");
//            throw new AlreadyVotedException("You have already voted on this complaint.");
        }

        // Save to SQL immediately (remarks must be persisted, not cached)
        Vote vote = new Vote();
        vote.setComplaint(complaint);
        vote.setUser(user);
        vote.setRemarks(castVoteRequest.getRemarks());
        vote.setVotedAt(LocalDateTime.now());
        vote.setSyncedFromRedis(false);
        voteRepository.save(vote);



        long voteCount = voteRedisService.getVoteCount(complaintId);

        VoteResponseDto response = VoteResponseDto.builder()
                .complaintId(castVoteRequest.getComplaintId())
                .voteCount(voteCount)
                .userHasVoted(true)
                .build();

        LOG.info("Vote cast successfully for complaintId={} by userId={}", castVoteRequest.getComplaintId(), userId);
        return ResponseUtil.getSuccessfulApiResponse(response, "Vote cast successfully");
    }

    // ── Remove Vote ───────────────────────────────────────────────────────────
   @Override
    public ApiResponse<?> removeVote(ComplaintIdRequest removeVoteRequest, Principal loggedInUser) {
        User user = userRepository.findByEmail(loggedInUser.getName());
        if(user == null) {
            return ResponseUtil.getSuccessfulApiResponse("User not found with email: " + loggedInUser.getName());
        }

        Long userId = user.getId();
       Complaint complaint = complaintRepository.findByUniqueId(removeVoteRequest.getComplaintId());
       if(complaint == null) {
           return ResponseUtil.getSuccessfulApiResponse("Complaint not found");
       }
       Long complaintId = complaint.getId();

        boolean removed = voteRedisService.removeVote(complaintId, userId);
        if (!removed) {
            return ResponseUtil.getFailureResponse("Add dependency with name matching 'VoteNotFoundException'");
//            throw new VoteNotFoundException("You have not voted on this complaint.");
        }

        long voteCount = voteRedisService.getVoteCount(complaintId);

        VoteResponseDto response = VoteResponseDto.builder()
                .complaintId(removeVoteRequest.getComplaintId())
                .voteCount(voteCount)
                .userHasVoted(false)
                .build();

        LOG.info("Vote removed successfully for complaintId={} by userId={}", removeVoteRequest.getComplaintId(), userId);
        return ResponseUtil.getSuccessfulApiResponse(response, "Vote removed successfully");
    }

    // ── Get Vote Status ───────────────────────────────────────────────────────
   @Override
    public ApiResponse<?> getVoteStatus(ComplaintIdRequest voteStatus, Principal loggedInUser) {
        User user = userRepository.findByEmail(loggedInUser.getName());
        if(user == null){
            return ResponseUtil.getFailureResponse("User not found with email: " + loggedInUser.getName());
        }

        Long userId = user.getId();
       Complaint complaint = complaintRepository.findByUniqueId(voteStatus.getComplaintId());
       if(complaint == null) {
           return ResponseUtil.getSuccessfulApiResponse("Complaint not found");
       }
       Long complaintId = complaint.getId();

        long voteCount = voteRedisService.getVoteCount(complaintId);
        if (voteCount == 0) {
            voteCount = voteRepository.countByComplaintId(complaintId);
        }

        boolean hasVoted = voteRedisService.hasVoted(complaintId, userId);
        if (!hasVoted) {
            hasVoted = voteRepository.existsByComplaintIdAndUserId(complaintId, userId);
        }

        VoteResponseDto response = VoteResponseDto.builder()
                .complaintId(voteStatus.getComplaintId())
                .voteCount(voteCount)
                .userHasVoted(hasVoted)
                .build();

        LOG.info("Vote status fetched for complaintId={} by userId={}", voteStatus.getComplaintId(), userId);
        return ResponseUtil.getSuccessfulApiResponse(response, "Vote status fetched successfully");
    }

}