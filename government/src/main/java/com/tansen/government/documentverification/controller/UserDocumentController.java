package com.tansen.government.documentverification.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.complaints.service.ComplaintService;
import com.tansen.government.documentverification.dto.ViewUserDocumentUniqueId;
import com.tansen.government.documentverification.service.DocumentVerificationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
@RestController
@RequestMapping(ApiConstant.MUNICIPALITY_API + ApiConstant.SLASH + ApiConstant.USERDOCUMENT)
public class UserDocumentController {


        private final DocumentVerificationService documentVerificationService;

        public UserDocumentController(DocumentVerificationService documentVerificationService) {
            this.documentVerificationService = documentVerificationService;
        }

        @PostMapping(ApiConstant.USER + ApiConstant.SLASH + ApiConstant.VIEW)
        public  ApiResponse<?> viewUserDocument(@RequestBody ViewUserDocumentUniqueId request, Principal loggedInAdmin){
            return documentVerificationService.viewUserDocument(request, loggedInAdmin);
        }
}
