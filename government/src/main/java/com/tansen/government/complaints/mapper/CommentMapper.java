package com.tansen.government.complaints.mapper;

import com.tansen.entity.Comment;
import com.tansen.government.complaints.dto.response.ListCommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class CommentMapper {

    public abstract ListCommentResponse entityToComment(Comment comment);
    public List<ListCommentResponse> listAllComment(List<Comment> comments) {
        return comments.stream().map(this::entityToComment).collect(Collectors.toList());
    }
}
