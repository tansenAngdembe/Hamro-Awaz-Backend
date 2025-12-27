package com.tansen.app.service;

import com.tansen.app.dto.request.*;
import com.tansen.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

public interface UserService {
    ApiResponse<?> createUser(CreateUserRequest request, HttpServletRequest httpServletRequest );
    ApiResponse<?> verifyUserAccount(VerifyUserAccountRequest request, HttpServletRequest httpServletRequest);
    ApiResponse<?> updateProfile(UserProfileUpdateRequest userProfileUpdateRequest, MultipartFile profilePicture, Principal connectedUser, HttpServletRequest request) throws IOException;
    ApiResponse<?> viewProfilePhoto(HttpServletRequest request , Principal connectedUser);
    ApiResponse<?> forgotPassword(ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest httpServletRequest);
    ApiResponse<?> sendAccountActivationEmail(SendAccountActivationEmailRequest request);
    ApiResponse<?> verifyForgotPasswordOtp(VerifyForgotPasswordOtpRequest forgotPasswordRequest, HttpServletRequest httpServletRequest);
    ApiResponse<?> resendForgotPasswordOtp(ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest httpServletRequest);
    ApiResponse<?> setForgetPassword(SetForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request);
    ApiResponse<?>  changePassword(ChangePasswordRequest changePasswordReset, Principal connectedUser, HttpServletRequest request);
    ApiResponse<?> viewProfile(Principal connectedUser, HttpServletRequest request);
}
