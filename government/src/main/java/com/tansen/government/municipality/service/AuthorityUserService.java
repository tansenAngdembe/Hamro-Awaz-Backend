package com.tansen.government.municipality.service;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.municipality.dto.*;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;

public interface AuthorityUserService {
    ApiResponse<?> listVendorUsers(SearchParam searchParam, Principal loggedInUser);
    ApiResponse<?> createVendorUser(CreateMunicipalityUserRequest createVendorUserRequest, Principal principal, HttpServletRequest request);
    ApiResponse<?> updateVendorUser(UpdateMunicipalityUserRequest updateVendorUserRequest, Principal loggedInAdmin, HttpServletRequest request);
    ApiResponse<?> blockVendorUser(BlockMunicipalityUserRequest blockVendorUserRequest, Principal loggedInAdmin, HttpServletRequest request);
    ApiResponse<?> setPassword(SetPasswordRequest setPasswordRequest);
    ApiResponse<?> changePassword(ChangePasswordRequest changePasswordRequest, Principal principal, HttpServletRequest request);
    ApiResponse<?> viewProfile(Principal loggedInVendor);
    ApiResponse<?> sendPasswordResetLink(SendPasswordResetLinkRequest sendPasswordResetLinkRequest, HttpServletRequest request);
    ApiResponse<?> editProfile(EditProfileRequest editProfileRequest, Principal principal, HttpServletRequest httpServletRequest);
    ApiResponse<?> forgotPassword(ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest httpServletRequest);
    ApiResponse<?> verifyForgotPasswordOtp(VerifyForgotPasswordOtpRequest forgotPasswordRequest, HttpServletRequest httpServletRequest);
    ApiResponse<?> resendForgotPasswordOtp(ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest httpServletRequest);
    ApiResponse<?> setForgetPassword(SetForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request);
    ApiResponse<?> deleteVendorUser(DeleteMunicipalityUserRequest deleteVendorUserRequest, HttpServletRequest request, Principal loggedInAdmin);
    ApiResponse<?> unblockVendorUser(UnblockMunicipalityUserRequest unblockVendorUserRequest, Principal loggedInVendor, HttpServletRequest request);
    ApiResponse<?> ViewUsersDetail(ViewUsersDetail viewUsersDetail, Principal principal);

    }
