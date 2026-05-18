package com.tansen.administrative.documentverification.controller;

import com.tansen.administrative.documentverification.dto.RejectUserDocumentRequest;
import com.tansen.administrative.documentverification.dto.VerifyUserDocumentRequest;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.administrative.documentverification.dto.ViewUserDocumentUniqueId;
import com.tansen.administrative.documentverification.service.DocumentVerificationService;
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

       @PostMapping(ApiConstant.USER + ApiConstant.SLASH + ApiConstant.VERIFY)
       public ApiResponse<?> verifyUserDocument(VerifyUserDocumentRequest request,
                                      Principal loggedInAdmin){
            return documentVerificationService.verifyUserDocument(request, loggedInAdmin);
       }
       @PostMapping(ApiConstant.USER + ApiConstant.SLASH + ApiConstant.REJECT)
       public ApiResponse<?> rejectUserDocument(RejectUserDocumentRequest request,
                                      Principal loggedInAdmin){
            return documentVerificationService.rejectUserDocument(request, loggedInAdmin);
       }
}
