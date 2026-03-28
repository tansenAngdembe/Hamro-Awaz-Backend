package com.tansen.app.comment.service;

import com.tansen.app.comment.dto.CreateCommentRequest;
import com.tansen.app.comment.dto.DeleteCommentRequest;
import com.tansen.app.comment.dto.UpdateCommentRequest;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.security.Principal;

public interface CommentService {
    ApiResponse<?>  getCommentBy(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedUser, HttpServletRequest httpServletRequest) throws IOException;


    ApiResponse<?> createComment(CreateCommentRequest createComment, Principal loggedUser, HttpServletRequest httpServletRequest ) throws IOException;
    ApiResponse<?> updateComment(UpdateCommentRequest updateCommentRequest, Principal loggedUser);
    ApiResponse<?> deleteComment(DeleteCommentRequest deleteCommentRequest, Principal loggedUser);



}
