package com.tansen.app.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCommentRequest {
    private String message;
    private String complaintUniqueId;
    private String commentUniqueId;
}
