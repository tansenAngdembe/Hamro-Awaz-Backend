package com.tansen.government.map.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ComplaintDto {
    private String complaintTitle;
    private CategoryDto category;
    private ComplaintStatusDto status;
    private UserDto reportedBy;
    private AuthorityUserDto assignedTo;
    private LocalDateTime createdDate;

}
