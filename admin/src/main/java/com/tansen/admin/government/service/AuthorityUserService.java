package com.tansen.admin.government.service;

import com.tansen.admin.government.dto.request.*;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface AuthorityUserService {
    ApiResponse<?> createAuthorityUser(CreateMunicipalityUserRequest createMunicipalityUserRequest, MultipartFile profilePicture, Principal loggedInUser, HttpServletRequest request) throws IOException;
    ApiResponse<?> getAuthorityUserList(SearchParam searchParam);
    ApiResponse<?> viewAuthorityUser(MunicipalityUserRequest request);
    ApiResponse<?> editAuthorityUser(EditMunicipalityUserRequest editMunicipalityUserRequest, MultipartFile profilePicture, Principal loggedInUser, HttpServletRequest request) throws IOException;
    ApiResponse<?> blockAuthorityUser(MunicipalityUserActionRequest municipalityUserActionRequest, Principal loggedInUser, HttpServletRequest request);
    ApiResponse<?> unblockAuthorityUser(MunicipalityUserActionRequest municipalityUserActionRequest, Principal loggedInUser, HttpServletRequest request);
    ApiResponse<?> deleteAuthorityUser(MunicipalityUserActionRequest municipalityUserActionRequest, Principal loggedInUser, HttpServletRequest request);
}
