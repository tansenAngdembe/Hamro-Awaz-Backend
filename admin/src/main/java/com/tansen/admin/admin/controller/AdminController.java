package com.tansen.admin.admin.controller;

import com.tansen.admin.admin.dto.request.*;
import com.tansen.admin.admin.service.AdminService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.ADMIN_API)
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(ApiConstant.CREATE)
    @PreAuthorize("hasAuthority('CREATE_ADMIN')")
    public ApiResponse<?> createAdmin(@RequestBody @Valid CreateAdminRequest createAdminRequest, Principal admin, HttpServletRequest httpServletRequest) {
        return adminService.createAdmin(createAdminRequest, admin, httpServletRequest);
    }

    @PostMapping(ApiConstant.LIST)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> listAllAdmins(@RequestBody @Valid SearchParam searchParam) {
        return adminService.listAllAdmins(searchParam);
    }

    @PostMapping(ApiConstant.BLOCK)
    @PreAuthorize("hasAnyAuthority('BLOCK_ADMIN')")
    public ApiResponse<?> blockAdmin(@RequestBody @Valid BlockAdminRequest blockAdminRequest, HttpServletRequest request, Principal admin) {
        return adminService.blockAdmin(blockAdminRequest, request, admin);
    }

    @PostMapping(ApiConstant.UPDATE)
    @PreAuthorize("hasAuthority('EDIT_ADMIN')")
    public ApiResponse<?> updateAdmin(@RequestBody @Valid UpdateAdminDetailRequest updateAdminDetailRequest, HttpServletRequest request, Principal admin) {
        return adminService.updateAdmin(updateAdminDetailRequest, request, admin);
    }

    @PostMapping(ApiConstant.DELETE)
    @PreAuthorize("hasAuthority('DELETE_ADMIN')")
    public ApiResponse<?> deleteAdmin(@RequestBody @Valid DeleteAdminRequest request, Principal principal, HttpServletRequest httpServletRequest) {
        return adminService.deleteAdmin(request, principal, httpServletRequest);
    }

    @PostMapping(ApiConstant.VIEW)
    @PreAuthorize("hasAuthority('VIEW_ADMIN')")
    public ApiResponse<?> viewAdminDetails(@RequestBody @Valid ViewAdminDetailRequest viewAdminDetailRequest, Principal loggedIn) {
        return adminService.viewAdminDetails(viewAdminDetailRequest, loggedIn);
    }

    @PostMapping(ApiConstant.SET_PASSWORD)
    public ApiResponse<?> setPassword(@RequestBody @Valid SetPasswordRequest setPasswordRequest) {
        return adminService.setPassword(setPasswordRequest);
    }

    @PostMapping(ApiConstant.EDIT_PROFILE)
    @PreAuthorize("hasAuthority('EDIT_ADMIN')")
    public ApiResponse<?> editProfile(
            @RequestPart(value = "details") EditProfileRequest editProfileRequest,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            Principal principal, HttpServletRequest httpServletRequest
    ) throws IOException {
        return adminService.editProfile(editProfileRequest,profilePicture, principal, httpServletRequest);
    }

    @PostMapping(ApiConstant.CHANGE_PASSWORD)
    public ApiResponse<?> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest, Principal principal, HttpServletRequest request) {
        return adminService.changePassword(changePasswordRequest, principal, request);
    }

    @PostMapping(ApiConstant.PASSWORD_RESET_LINK)
    @PreAuthorize("hasAuthority('SEND_ADMIN_PASSWORD_RESET_LINK')")
    public ApiResponse<?> passwordReset(@RequestBody @Valid SendPasswordResetLinkRequest sendPasswordResetLinkRequest, HttpServletRequest request) {
        return adminService.sendPasswordResetLink(sendPasswordResetLinkRequest, request);
    }

    @PostMapping(ApiConstant.UNBLOCK)
    @PreAuthorize("hasAuthority('UNBLOCK_ADMIN')")
    public ApiResponse<?> unblockAdmin(@RequestBody @Valid UnblockAdminRequest unblockAdminRequest, HttpServletRequest request, Principal loggedIn) {
        return adminService.unblockAdmin(unblockAdminRequest, request, loggedIn);
    }

    @PostMapping(ApiConstant.VIEW + ApiConstant.SLASH + ApiConstant.PROFILE)
    public ApiResponse<?> viewProfile(Principal loggedInAdmin) {
        return adminService.viewProfile(loggedInAdmin);
    }

    @PostMapping(ApiConstant.RESET_PASSWORD)
    public ApiResponse<?> resetPassword(@RequestBody @Valid SetPasswordRequest setPasswordRequest) {
        return adminService.setPassword(setPasswordRequest);
    }
}

