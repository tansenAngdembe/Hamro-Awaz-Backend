package com.tansen.administrative.complaints.dto.response;

import com.tansen.administrative.complaints.dto.UserDto;
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
