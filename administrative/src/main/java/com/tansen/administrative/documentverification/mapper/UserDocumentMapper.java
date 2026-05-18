package com.tansen.administrative.documentverification.mapper;

import com.tansen.entity.UserDocuments;
import com.tansen.administrative.documentverification.dto.UserDocumentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserDocumentMapper {
    public abstract UserDocumentResponseDto entityToUserDocument(UserDocuments userDocuments);

}
