package com.tansen.app.vote.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintIdRequest {
    @NotBlank(message = "Complaint ID is required")
    private String complaintId;
}
