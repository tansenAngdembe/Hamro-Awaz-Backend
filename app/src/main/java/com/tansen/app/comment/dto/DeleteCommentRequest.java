package com.tansen.app.comment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteCommentRequest {
    private String complaintUniqueId;
    private String commentUniqueId;
}
