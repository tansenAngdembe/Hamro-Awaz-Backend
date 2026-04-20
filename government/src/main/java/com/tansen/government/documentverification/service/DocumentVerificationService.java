package com.tansen.government.documentverification.service;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.documentverification.dto.ViewUserDocumentUniqueId;

import java.security.Principal;

public interface DocumentVerificationService {
//    ApiResponse<?> listDocumentVerificationReq(SearchParam searchParam, Principal loggedInAdmin);
    ApiResponse<?> viewUserDocument(ViewUserDocumentUniqueId request, Principal loggedInAdmin);

}
