package com.tansen.admin.complaints.dto.response;

import com.tansen.admin.complaints.dto.*;
import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ComplaintResponse extends ModelBase {
    private String complaintTitle;
    private CategoryDto category;
    private ComplaintStatusDto status;
    private UserDto reportedBy;
    private AuthorityUserDto assignedTo;
    private LocalDateTime resolvedAt;
    private PriorityDto priority;
    private LocalDateTime createdDate;
    private List<CommentDto> comments;

}
