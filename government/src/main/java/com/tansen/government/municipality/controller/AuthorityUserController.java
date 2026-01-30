package com.tansen.government.municipality.controller;

import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.municipality.dto.*;
import com.tansen.government.municipality.service.AuthorityUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.MUNICIPALITY_API)
public class AuthorityUserController {
    private final AuthorityUserService authorityUserService;

    public AuthorityUserController(AuthorityUserService vendorUserService) {
        this.authorityUserService = vendorUserService;
    }

    @PostMapping(ApiConstant.USERS + ApiConstant.SLASH + ApiConstant.LIST)
    @PreAuthorize("hasAuthority('VIEW_STAFF')")
    public ApiResponse<?> listVendor(@RequestBody @Valid SearchParam searchParam, Principal loggedInUser) {
        return authorityUserService.listVendorUsers(searchParam, loggedInUser);
    }
    @PostMapping(ApiConstant.USERS + ApiConstant.SLASH +ApiConstant.CREATE)
    @PreAuthorize("hasAuthority('CREATE_STAFF')")
    public ApiResponse<?> createVendorUser(@RequestBody @Valid CreateMunicipalityUserRequest createVendorUserRequest, Principal loggedInAdmin, HttpServletRequest request) {
        return authorityUserService.createVendorUser(createVendorUserRequest, loggedInAdmin, request);
    }
    @PostMapping(ApiConstant.USERS + ApiConstant.SLASH +ApiConstant.UPDATE)
    @PreAuthorize("hasAuthority('EDIT_STAFF')")
    public ApiResponse<?> updateVendorUser(@RequestBody @Valid UpdateMunicipalityUserRequest updateVendorUserRequest, Principal loggedInAdmin, HttpServletRequest request) {
        return authorityUserService.updateVendorUser(updateVendorUserRequest, loggedInAdmin, request);
    }
   @PostMapping(ApiConstant.USERS + ApiConstant.SLASH +ApiConstant.DELETE)
    @PreAuthorize("hasAuthority('DELETE_STAFF')")
    public ApiResponse<?> deleteVendorUser(@RequestBody @Valid DeleteMunicipalityUserRequest deleteVendorUserRequest, Principal loggedInAdmin, HttpServletRequest request){
        return  authorityUserService.deleteVendorUser(deleteVendorUserRequest, request,loggedInAdmin);
   }
   @PostMapping(ApiConstant.USERS + ApiConstant.SLASH +ApiConstant.UNBLOCK)
    @PreAuthorize("hasAuthority('UNBLOCK_STAFF')")
    public ApiResponse<?>unblockVendorUser(@Valid @RequestBody UnblockMunicipalityUserRequest unblockVendorUserRequest, Principal loggedInVendor, HttpServletRequest request){
        return authorityUserService.unblockVendorUser(unblockVendorUserRequest,loggedInVendor,request);
   }

    @PostMapping(ApiConstant.USERS + ApiConstant.SLASH + ApiConstant.BLOCK)
    @PreAuthorize("hasAuthority('BLOCK_USER')")
    public ApiResponse<?> blockVendorUser(@RequestBody @Valid BlockMunicipalityUserRequest blockVendorUserRequest, Principal loggedInAdmin, HttpServletRequest request) {
        return authorityUserService.blockVendorUser(blockVendorUserRequest, loggedInAdmin, request);
    }

    @PostMapping(ApiConstant.USERS + ApiConstant.SLASH + ApiConstant.SET_PASSWORD)
    public ApiResponse<?> setPassword(@RequestBody @Valid SetPasswordRequest setPasswordRequest) {
        return authorityUserService.setPassword(setPasswordRequest);
    }

    @PostMapping(ApiConstant.USERS + ApiConstant.SLASH + ApiConstant.CHANGE_PASSWORD)
    @PreAuthorize("hasAuthority('SEND_STAFF_PASSWORD_RESET_LINK')")
    public ApiResponse<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest, Principal principal, HttpServletRequest request) {
        return authorityUserService.changePassword(changePasswordRequest, principal, request);
    }

    @PostMapping(ApiConstant.USERS + ApiConstant.SLASH + ApiConstant.VIEW + ApiConstant.SLASH + ApiConstant.PROFILE)
    @PreAuthorize("hasAuthority('STAFF')")
    public ApiResponse<?> viewProfile(Principal loggedInVendor) {
        return authorityUserService.viewProfile(loggedInVendor);
    }
    @PostMapping(ApiConstant.USERS + ApiConstant.SLASH + ApiConstant.PASSWORD_RESET_LINK)
    @PreAuthorize("hasAuthority('SEND_STAFF_PASSWORD_RESET_LINK')")
    public  ApiResponse<?> sendPasswordResetLink(@Valid @RequestBody SendPasswordResetLinkRequest sendPasswordResetLinkRequest, HttpServletRequest request){
        return authorityUserService.sendPasswordResetLink(sendPasswordResetLinkRequest, request);
    }

    @PostMapping(ApiConstant.USERS + ApiConstant.SLASH + ApiConstant.EDIT_PROFILE)
    @PreAuthorize("hasAuthority('STAFF')")
    public  ApiResponse<?> editProfile(@Valid @RequestBody EditProfileRequest editProfileRequest, Principal principal, HttpServletRequest httpServletRequest){
        return authorityUserService.editProfile(editProfileRequest, principal, httpServletRequest);
    }
    @PostMapping(ApiConstant.USERS+ApiConstant.SLASH +ApiConstant.VIEW)
    @PreAuthorize("hasAuthority('VIEW_USER')")
    public ApiResponse<?> viewUsersDetail(@Valid @RequestBody ViewUsersDetail viewUsersDetail, Principal principal){
        return authorityUserService.ViewUsersDetail(viewUsersDetail, principal);
    }
    @PostMapping(ApiConstant.FORGOT_PASSWORD)
    @PreAuthorize("hasAuthority('STAFF')")
    public ApiResponse<?> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest,HttpServletRequest request) {
        return authorityUserService.forgotPassword(forgotPasswordRequest, request);
    }
    @PostMapping(ApiConstant.FORGOT_PASSWORD + ApiConstant.SLASH + ApiConstant.VERIFY_OTP)
    @PreAuthorize("hasAuthority('STAFF')")
    public ApiResponse<?> verifyForgotPasswordOtp(@Valid @RequestBody VerifyForgotPasswordOtpRequest forgotPasswordRequest, HttpServletRequest request) {
        return authorityUserService.verifyForgotPasswordOtp(forgotPasswordRequest, request);
    }

    @PostMapping(ApiConstant.FORGOT_PASSWORD + ApiConstant.SLASH + ApiConstant.SET)
    @PreAuthorize("hasAuthority('STAFF')")
    public ApiResponse<?> setForgetPassword(@Valid @RequestBody SetForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) {
        return authorityUserService.setForgetPassword(forgotPasswordRequest, request);
    }

    @PostMapping(ApiConstant.FORGOT_PASSWORD + ApiConstant.SLASH + ApiConstant.RESEND_OTP)
    @PreAuthorize("hasAuthority('RESEND_STAFF_ACCOUNT_ACTIVATION_LINK')")
    public ApiResponse<?> resendForgotPasswordOtp(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) {
        return authorityUserService.resendForgotPasswordOtp(forgotPasswordRequest, request);
    }

}
