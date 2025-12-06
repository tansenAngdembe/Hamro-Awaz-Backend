package com.tansen.admin.admin.service;

import com.tansen.admin.admin.dto.request.*;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface AdminService {
    ApiResponse<?> createAdmin(CreateAdminRequest createAdminRequest, Principal admin, HttpServletRequest httpServletRequest);
    ApiResponse<?> listAllAdmins(SearchParam searchParam);
    ApiResponse<?> viewAdminDetails(ViewAdminDetailRequest viewAdminDetailRequest, Principal loggedIn);
    ApiResponse<?> updateAdmin(UpdateAdminDetailRequest updateAdminDetailRequest, HttpServletRequest request, Principal admin);
    ApiResponse<?> viewProfile(Principal loggedInAdmin);
    ApiResponse<?> editProfile(EditProfileRequest editProfileRequest, MultipartFile profilePicture , Principal principal, HttpServletRequest httpServletRequest) throws IOException;
    ApiResponse<?> blockAdmin(BlockAdminRequest blockAdminRequest, HttpServletRequest request, Principal admin);
    ApiResponse<?> unblockAdmin(UnblockAdminRequest unblockAdminRequest, HttpServletRequest request, Principal loggedIn);
    ApiResponse<?> deleteAdmin(DeleteAdminRequest request, Principal principal, HttpServletRequest httpServletRequest);
    ApiResponse<?> setPassword(SetPasswordRequest setPasswordRequest);
    ApiResponse<?> changePassword(ChangePasswordRequest changePasswordRequest, Principal principal, HttpServletRequest request);
    ApiResponse<?> sendPasswordResetLink(SendPasswordResetLinkRequest sendPasswordResetLinkRequest, HttpServletRequest request);
}