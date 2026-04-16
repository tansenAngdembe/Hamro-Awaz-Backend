package com.tansen.app.vote.service;

import com.tansen.app.vote.dto.CastVoteRequest;
import com.tansen.app.vote.dto.ComplaintIdRequest;
import com.tansen.app.vote.dto.VoteResponseDto;
import com.tansen.common.dto.ApiResponse;

import java.security.Principal;

public interface VoteService {
    ApiResponse<?> castVote(CastVoteRequest castVoteRequest, Principal loggedInUser);
    ApiResponse<?> removeVote(ComplaintIdRequest removeVoteRequest, Principal loggedInUser);
    ApiResponse<?> getVoteStatus(ComplaintIdRequest voteStatus, Principal loggedInUser);

    }
