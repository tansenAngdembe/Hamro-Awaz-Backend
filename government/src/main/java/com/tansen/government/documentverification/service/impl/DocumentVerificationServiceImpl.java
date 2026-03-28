package com.tansen.government.documentverification.service.impl;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.documentverification.service.DocumentVerificationService;
import org.springframework.stereotype.Service;

import java.security.Principal;
@Service
public class DocumentVerificationServiceImpl implements DocumentVerificationService {

    @Override
    public ApiResponse<?> listDocumentVerificationReq(SearchParam searchParam, Principal loggedInAdmin){
        return null;
    }
 }
