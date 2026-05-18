package com.tansen.administrative.complaints.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class CommentDto extends ModelBase {
    private String message;
    private LocalDateTime commentAt;
    private LocalDateTime updatedAt;
    private String uniqueId;
    private String createdIp;
    private Boolean isDelete;
    private UserCommentResponse commentBy;


}
