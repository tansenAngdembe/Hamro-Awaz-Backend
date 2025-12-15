package com.tansen.admin.government.controller;

import com.cosmotech.admin.vendor.dto.request.CreateVendorUserRequest;
import com.cosmotech.admin.vendor.dto.request.EditVendorUserRequest;
import com.cosmotech.admin.vendor.dto.request.VendorUserActionRequest;
import com.cosmotech.admin.vendor.dto.request.VendorUserRequest;
import com.cosmotech.admin.vendor.service.VendorUserService;
import com.cosmotech.common.constant.ApiConstant;
import com.cosmotech.common.dto.ApiResponse;
import com.cosmotech.common.dto.SearchParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.ADMIN_API + ApiConstant.SLASH + ApiConstant.VENDOR + ApiConstant.SLASH + ApiConstant.USERS)
public class MunicipalityUserController {
    private final VendorUserService vendorUserService;

    public MunicipalityUserController(VendorUserService vendorUserService) {
        this.vendorUserService = vendorUserService;
    }

    @PostMapping(ApiConstant.CREATE)
    @PreAuthorize("hasAuthority('CREATE_VENDOR')")
    public ApiResponse<?> createVendorUser(
            @Valid @RequestPart(value = "vendorUser") CreateVendorUserRequest createVendorUserRequest,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            Principal loggedInUser,
            HttpServletRequest request) throws IOException {
        return vendorUserService.createVendorUser(createVendorUserRequest, profilePicture, loggedInUser, request);
    }

    @PostMapping(ApiConstant.UPDATE)
    @PreAuthorize("hasAuthority('EDIT_VENDOR')")
    public ApiResponse<?> editVendorUser(
            @Valid @RequestPart(value = "vendorUser") EditVendorUserRequest editVendorUserRequest,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            Principal loggedInUser,
            HttpServletRequest request) throws IOException {
        return vendorUserService.editVendorUser(editVendorUserRequest, profilePicture, loggedInUser, request);
    }

    @PostMapping(ApiConstant.LIST)
    @PreAuthorize("hasAuthority('VIEW_VENDOR')")
    public ApiResponse<?> getVendorUserList(@RequestBody SearchParam searchParam) {
        return vendorUserService.getVendorUserList(searchParam);
    }

    @PostMapping(ApiConstant.VIEW)
    @PreAuthorize("hasAuthority('VIEW_VENDOR')")
    public ApiResponse<?> viewVendorUserDetails(@Valid @RequestBody VendorUserRequest request) {
        return vendorUserService.viewVendorUser(request);
    }

    @PostMapping(ApiConstant.BLOCK)
    @PreAuthorize("hasAuthority('BLOCK_VENDOR')")
    public ApiResponse<?> blockVendorUser(@Valid @RequestBody VendorUserActionRequest vendorUserActionRequest, Principal loggedInUser, HttpServletRequest request) {
        return vendorUserService.blockVendorUser(vendorUserActionRequest, loggedInUser, request);
    }

    @PostMapping(ApiConstant.UNBLOCK)
    @PreAuthorize("hasAuthority('UNBLOCK_VENDOR')")
    public ApiResponse<?> unblockVendorUser(@Valid @RequestBody VendorUserActionRequest vendorUserActionRequest, Principal loggedInUser, HttpServletRequest request) {
        return vendorUserService.unblockVendorUser(vendorUserActionRequest, loggedInUser, request);
    }

    @PostMapping(ApiConstant.DELETE)
    @PreAuthorize("hasAuthority('DELETE_VENDOR')")
    public ApiResponse<?> deleteVendorUser(@Valid @RequestBody VendorUserActionRequest vendorUserActionRequest, Principal loggedInUser, HttpServletRequest request) {
        return vendorUserService.deleteVendorUser(vendorUserActionRequest, loggedInUser, request);
    }
}
