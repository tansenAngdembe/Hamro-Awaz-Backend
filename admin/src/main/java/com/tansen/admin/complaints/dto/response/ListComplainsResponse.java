package com.tansen.admin.complaints.dto.response;

import com.tansen.admin.complaints.dto.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ListComplainsResponse {
    private String uniqueId;
    private String complaintTitle;
    private CategoryDto category;
    private ComplaintStatusDto status;
    private UserDto reportedBy;
    private AuthorityUserDto assignedTo;
    private LocalDateTime resolvedAt;
    private PriorityDto priority;
    private LocalDateTime createdDate;

}
