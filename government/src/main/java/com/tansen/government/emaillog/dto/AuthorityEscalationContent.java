package com.tansen.government.emaillog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

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
