package com.tansen.administrative.documentverification.service;

import com.tansen.administrative.documentverification.dto.RejectUserDocumentRequest;
import com.tansen.administrative.documentverification.dto.VerifyUserDocumentRequest;
import com.tansen.common.dto.ApiResponse;
import com.tansen.administrative.documentverification.dto.ViewUserDocumentUniqueId;

import java.security.Principal;

public interface DocumentVerificationService {
//    ApiResponse<?> listDocumentVerificationReq(SearchParam searchParam, Principal loggedInAdmin);
    ApiResponse<?> viewUserDocument(ViewUserDocumentUniqueId request, Principal loggedInAdmin);
    ApiResponse<?> verifyUserDocument(VerifyUserDocumentRequest request,
                                      Principal loggedInAdmin);
    ApiResponse<?> rejectUserDocument(RejectUserDocumentRequest request,
                                             Principal loggedInAdmin);
}
