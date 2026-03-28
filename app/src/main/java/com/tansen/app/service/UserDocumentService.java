package com.tansen.app.service;

import com.tansen.app.dto.request.UploadDocumentRequest;
import com.tansen.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

public interface UserDocumentService {
    ApiResponse<?> uploadDocument(UploadDocumentRequest uploadDocumentRequest,
                                  MultipartFile citizenshipFront,
                                  MultipartFile citizenshipBack,
                                  Principal principal,
                                  HttpServletRequest httpServletRequest);
//
//    ApiResponse<?> resubmitDocument(
//            UploadDocumentRequest uploadDocumentRequest,
//            MultipartFile citizenshipFront,
//            MultipartFile citizenshipBack,
//            Principal principal);
}
