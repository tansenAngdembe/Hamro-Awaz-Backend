package com.tansen.government.complaints.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuthorityUserDto {
    private String complaintTitle;
    private String complaintRule;
    private String category;
    private LocalDateTime createdDate;
    private String priority;
}
