package com.tansen.administrative.emaillog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuthorityEscalationContent {

    @NotBlank(message = "Email template is required")
    private String template;

    @NotBlank(message = "Name is required")
    private String complaintTitle;
    private String category;
    private LocalDateTime createdDate;
    private String complaintRule;
    private String assignedTo;
    private LocalDateTime escalationAt;

}
