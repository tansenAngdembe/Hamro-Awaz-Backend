package com.tansen.app.vote.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CastVoteRequest {
    @NotBlank(message = "Complaint ID is required")
    private String complaintId;
    @NotBlank(message = "Remarks is required")
    private String remarks;
}
