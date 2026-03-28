package com.tansen.app.comment.dto;

import com.tansen.app.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ListCommentResponse {
    private String message;
    private LocalDateTime commentAt;
    private LocalDateTime updatedAt;
    private String uniqueId;
    private UserDto commentBy;

}
