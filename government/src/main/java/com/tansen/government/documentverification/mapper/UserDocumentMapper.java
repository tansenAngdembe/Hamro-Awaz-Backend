package com.tansen.government.documentverification.mapper;

import com.tansen.entity.Comment;
import com.tansen.entity.UserDocuments;
import com.tansen.government.complaints.dto.response.ListCommentResponse;
import com.tansen.government.documentverification.dto.UserDocumentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserDocumentMapper {
    public abstract UserDocumentResponseDto entityToUserDocument(UserDocuments userDocuments);

}
