package com.tansen.app.comment.controller;

import com.tansen.app.comment.dto.CreateCommentRequest;
import com.tansen.app.comment.dto.DeleteCommentRequest;
import com.tansen.app.comment.dto.UpdateCommentRequest;
import com.tansen.app.comment.service.CommentService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;

@RequestMapping(path = ApiConstant.USER_API + ApiConstant.SLASH +  ApiConstant.COMMENT)
@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(ApiConstant.CREATE)
    public ApiResponse<?> createComment(@RequestBody CreateCommentRequest createComment, Principal loggedUser, HttpServletRequest httpServletRequest ) throws IOException{
        return commentService.createComment(createComment, loggedUser, httpServletRequest);
    }
    @PostMapping(ApiConstant.VIEW)
    public ApiResponse<?>  getCommentBy(@RequestBody ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedUser, HttpServletRequest httpServletRequest) throws IOException{
        return commentService.getCommentBy(complaintUniqueIdDto, loggedUser, httpServletRequest);
    }
    @PostMapping(ApiConstant.UPDATE)
    public ApiResponse<?> updateComment(@RequestBody UpdateCommentRequest updateCommentRequest, Principal loggedUser){
       return  commentService.updateComment(updateCommentRequest, loggedUser);
    }
    @PostMapping(ApiConstant.DELETE)
    public ApiResponse<?> deleteComment(@RequestBody DeleteCommentRequest deleteCommentRequest, Principal loggedUser){
        return commentService.deleteComment(deleteCommentRequest, loggedUser);
    }




}
