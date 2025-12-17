package com.tansen.admin.government.service;

import com.tansen.admin.government.dto.request.*;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface MunicipalityService {
    ApiResponse<?> createMunicipality(CreateMunicipalityRequest createMunicipalityRequest, MultipartFile documentFile, Principal principal, HttpServletRequest httpServletRequest) throws IOException;
    ApiResponse<?> editMunicipality(EditMunicipalityRequest editMunicipalityRequest, MultipartFile documentFile, Principal loggedInUser, HttpServletRequest httpServletRequest) throws IOException;
    ApiResponse<?> getMunicipalityList(SearchParam searchParam);
    ApiResponse<?> viewMunicipalityDetails(MunicipalityRequest request);
    ApiResponse<?> blockMunicipality(MunicipalityActionRequest  blockMunicipalityRequest, HttpServletRequest request, Principal loggedInUser);
    ApiResponse<?> unblockMunicipality(MunicipalityActionRequest unblockMunicipalityRequest, HttpServletRequest request, Principal loggedInUser);
    ApiResponse<?> deleteMunicipality(MunicipalityActionRequest deleteMunicipalityRequest, HttpServletRequest request, Principal loggedInUser);

}

