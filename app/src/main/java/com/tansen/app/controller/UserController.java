package com.tansen.app.controller;



import com.tansen.app.dto.request.*;
import com.tansen.app.service.UserService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RequestMapping(ApiConstant.USER_API)
@RestController
public class UserController {
    private final UserService userProfileService;

    public UserController(UserService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping(ApiConstant.CREATE)
    public ApiResponse<?> createUser(@Valid @RequestBody CreateUserRequest userProfileUpdateRequest, HttpServletRequest request) {
        return userProfileService.createUser(userProfileUpdateRequest, request);
    }

    @PostMapping(ApiConstant.ACCOUNT + ApiConstant.SLASH + ApiConstant.VERIFY)
    public ApiResponse<?> verifyUserAccount(@Valid @RequestBody VerifyUserAccountRequest verifyUserAccountRequest, HttpServletRequest request) {
        return userProfileService.verifyUserAccount(verifyUserAccountRequest, request);
    }

    @PostMapping(ApiConstant.ACCOUNT +ApiConstant.SLASH + ApiConstant.VERIFY + ApiConstant.SLASH + ApiConstant.RESEND_OTP)
    public ApiResponse<?> sendAccountActivationEmail(@RequestBody @Valid SendAccountActivationEmailRequest request) {
        return userProfileService.sendAccountActivationEmail(request);
    }

    @PostMapping(value = ApiConstant.UPDATE,consumes = {"multipart/form-data"})
    public ApiResponse<?> updateProfile(@Valid @RequestPart(value = "userUpdate", required = false) UserProfileUpdateRequest userProfileUpdateRequest,
                                        @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
                                         Principal connectedUser,HttpServletRequest request
                                         )throws IOException {
        return userProfileService.updateProfile(userProfileUpdateRequest, profilePicture, connectedUser, request);
    }

    @PostMapping(ApiConstant.PHOTO + ApiConstant.SLASH + ApiConstant.VIEW)
    public ApiResponse<?> viewProfilePhoto(HttpServletRequest request, Principal connectedUser) {
        return userProfileService.viewProfilePhoto(request, connectedUser);
    }


    @PostMapping(ApiConstant.FORGOT_PASSWORD)
    public ApiResponse<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) {
        return userProfileService.forgotPassword(forgotPasswordRequest, request);
    }

    @PostMapping(ApiConstant.FORGOT_PASSWORD + ApiConstant.SLASH + ApiConstant.VERIFY_OTP)
    public ApiResponse<?> verifyForgotPasswordOtp(@Valid @RequestBody VerifyForgotPasswordOtpRequest forgotPasswordRequest, HttpServletRequest request) {
        return userProfileService.verifyForgotPasswordOtp(forgotPasswordRequest, request);
    }

    @PostMapping(ApiConstant.FORGOT_PASSWORD + ApiConstant.SLASH + ApiConstant.SET)
    public ApiResponse<?> setForgetPassword(@Valid @RequestBody SetForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) {
        return userProfileService.setForgetPassword(forgotPasswordRequest, request);
    }

    @PostMapping(ApiConstant.FORGOT_PASSWORD + ApiConstant.SLASH + ApiConstant.RESEND_OTP)
    public ApiResponse<?> resendForgotPasswordOtp(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) {
        return userProfileService.resendForgotPasswordOtp(forgotPasswordRequest, request);
    }
    @PostMapping(ApiConstant.CHANGE_PASSWORD)
    public ApiResponse<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest, Principal connectedUser, HttpServletRequest request) {
        return userProfileService.changePassword(changePasswordRequest, connectedUser, request);
    }

    @PostMapping(ApiConstant.PROFILE)
    public ApiResponse<?> viewProfile(Principal connectedUser, HttpServletRequest request) {
        return userProfileService.viewProfile(connectedUser, request);
    }
}
