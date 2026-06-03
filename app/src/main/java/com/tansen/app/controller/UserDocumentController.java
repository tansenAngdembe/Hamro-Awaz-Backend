package com.tansen.app.controller;

import com.tansen.app.dto.request.UploadDocumentRequest;
import com.tansen.app.service.UserDocumentService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RequestMapping(ApiConstant.USER_API + ApiConstant.SLASH + ApiConstant.DOC)
@RestController
public class UserDocumentController{
 private final UserDocumentService userDocumentService;

    public UserDocumentController(UserDocumentService userDocumentService) {
        this.userDocumentService = userDocumentService;
    }


    @PostMapping(value = ApiConstant.SLASH + ApiConstant.UPLOAD, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> uploadDocuments(
            @RequestPart("data") UploadDocumentRequest request,
            @RequestPart(value = "citizenshipFront", required = false) MultipartFile citizenshipFront,
            @RequestPart(value = "citizenshipBack", required = false) MultipartFile citizenshipBack,
            Principal principal,
            HttpServletRequest httpServletRequest) throws IOException {
        return userDocumentService.uploadDocument(request, citizenshipFront, citizenshipBack, principal,httpServletRequest);
    }

}
