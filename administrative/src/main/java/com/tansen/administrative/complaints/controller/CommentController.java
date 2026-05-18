package com.tansen.administrative.complaints.controller;


import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import com.tansen.administrative.complaints.service.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.MUNICIPALITY_API + ApiConstant.SLASH + ApiConstant.COMMENT)
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(ApiConstant.LIST)
    @PreAuthorize("hasAuthority('STAFF')")
    public  ApiResponse<?> listAllComment(@RequestBody ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin){
        return commentService.listAllComment(complaintUniqueIdDto, loggedInAdmin);
    }

}
