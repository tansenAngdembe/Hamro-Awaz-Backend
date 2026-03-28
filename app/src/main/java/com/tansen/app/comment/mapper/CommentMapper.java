package com.tansen.app.comment.mapper;

import com.tansen.app.comment.dto.CreateCommentRequest;
import com.tansen.app.comment.dto.ListCommentResponse;
import com.tansen.app.comment.dto.UpdateCommentRequest;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.Comment;
import com.tansen.entity.Complaint;
import com.tansen.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class CommentMapper {

        public abstract ListCommentResponse entityToComment(Comment comment);
        public List<ListCommentResponse> listAllComment(List<Comment> comments) {
            return comments.stream().map(this::entityToComment).collect(Collectors.toList());
        }
        public Comment entityToCreateComment(CreateCommentRequest createComment, Complaint complaint, User user, HttpServletRequest request) {
            Comment newComment = new Comment();
            newComment.setMessage(createComment.getMessage().trim());
            newComment.setComplaint(complaint);
            newComment.setCommentBy(user);
            newComment.setIsDelete(false);
            newComment.setUniqueId(UuidUtil.generateUuid());
            newComment.setCommentAt(LocalDateTime.now());
            newComment.setCreatedIp(request.getRemoteAddr());

            return  newComment;

        }

        public Comment updateComment(Comment indComment, UpdateCommentRequest updateCommentRequest) {
            indComment.setUpdatedAt(LocalDateTime.now());
            indComment.setMessage(updateCommentRequest.getMessage());
            return indComment;
        }
}


