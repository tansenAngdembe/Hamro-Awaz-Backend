package com.tansen.app.mapper;

import com.tansen.app.dto.request.UploadDocumentRequest;
import com.tansen.common.constant.FilePathConstant;
import com.tansen.common.service.UploadFileService;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.UserDocuments;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserDocumentMapper {
    @Autowired
    private UploadFileService uploadFileService;

    public UserDocuments mapToUserDocuments( UploadDocumentRequest userDocumentRequest, MultipartFile citizenshipFront, MultipartFile citizenshipBack) throws IOException {

        UserDocuments userDocuments = new UserDocuments();

        if(!citizenshipFront.isEmpty() && !citizenshipBack.isEmpty()){
            userDocuments.setCitizenshipCardFront(
                    uploadFileService.uploadFile(citizenshipFront, FilePathConstant.BASE_PATH,FilePathConstant.CITIZENSHIPCARDFRONT, true )
            );
            userDocuments.setCitizenshipCardBack(
                    uploadFileService.uploadFile(citizenshipBack,FilePathConstant.BASE_PATH,FilePathConstant.CITIZENSHIPCARDBACK,true));
        }
        userDocuments.setNationalIdentityNumber(userDocumentRequest.getNationalIdentityNumber());
        userDocuments.setUpdatedAt(LocalDateTime.now());
        userDocuments.setUniqueId(UuidUtil.generateUuid());
        userDocuments.setIsDocumentVerified(false);

        if (userDocuments.getCreatedAt() == null) {
            userDocuments.setCreatedAt(LocalDateTime.now());
        }


        return userDocuments;
    }

}
