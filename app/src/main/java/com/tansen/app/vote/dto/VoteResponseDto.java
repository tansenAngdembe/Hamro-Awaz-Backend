package com.tansen.app.vote.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoteResponseDto {
    private String complaintId;
    private long voteCount;
    private boolean userHasVoted;
    private String message;
}
