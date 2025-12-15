package com.tansen.admin.government.controller;

import com.tansen.common.constant.ApiConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.ADMIN_API)
public class MunicipalityController {
    private final VendorService vendorService;

    public MunicipalityController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping(ApiConstant.VENDOR + ApiConstant.SLASH + ApiConstant.CREATE)
    @PreAuthorize("hasAuthority('CREATE_VENDOR')")
    public ApiResponse<?> createVendor(
            @Valid @RequestPart("vendor") CreateVendorRequest request,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile,
            @RequestPart(value = "documentFile",required = false) MultipartFile documentFile,
            Principal loggedInUser,
            HttpServletRequest httpServletRequest) throws IOException {
        return vendorService.createVendor(request, logoFile, documentFile, loggedInUser, httpServletRequest);
    }

    @PostMapping(ApiConstant.VENDOR + ApiConstant.SLASH + ApiConstant.UPDATE)
    @PreAuthorize("hasAuthority('EDIT_VENDOR')")
    public ApiResponse<?> editVendor(
            @Valid @RequestPart("vendor") EditVendorRequest request,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile,
            @RequestPart(value = "documentFile", required = false) MultipartFile documentFile,
            Principal loggedInUser,
            HttpServletRequest httpServletRequest) throws IOException {
        return vendorService.editVendor(request, logoFile, documentFile, loggedInUser, httpServletRequest);
    }

    @PostMapping(ApiConstant.VENDOR + ApiConstant.SLASH + ApiConstant.LIST)
    @PreAuthorize("hasAuthority('VIEW_VENDOR')")
    public ApiResponse<?> listAllVendors(@Valid @RequestBody SearchParam searchParam) {
        return vendorService.getVendorList(searchParam);
    }

    @PostMapping(ApiConstant.VENDOR + ApiConstant.SLASH + ApiConstant.VIEW)
    @PreAuthorize("hasAuthority('VIEW_VENDOR')")
    public ApiResponse<?> viewVendorDetails(
            @Valid @RequestBody VendorRequest request) {
        return vendorService.viewVendorDetails(request);
    }

    @PostMapping(ApiConstant.VENDOR + ApiConstant.SLASH + ApiConstant.BLOCK)
    @PreAuthorize("hasAuthority('BLOCK_VENDOR')")
    public ApiResponse<?> blockVendor(
            @Valid @RequestBody VendorActionRequest actionRequest,
            HttpServletRequest request,
            Principal loggedInUser) {
        return vendorService.blockVendor(actionRequest, request, loggedInUser);
    }

    @PostMapping(ApiConstant.VENDOR + ApiConstant.SLASH + ApiConstant.UNBLOCK)
    @PreAuthorize("hasAuthority('UNBLOCK_VENDOR')")
    public ApiResponse<?> unblockVendor(
            @Valid @RequestBody VendorActionRequest actionRequest,
            HttpServletRequest request,
            Principal loggedInUser) {
        return vendorService.unblockVendor(actionRequest, request, loggedInUser);
    }

    @PostMapping(ApiConstant.VENDOR + ApiConstant.SLASH + ApiConstant.DELETE)
    @PreAuthorize("hasAuthority('DELETE_VENDOR')")
    public ApiResponse<?> deleteVendor(
            @Valid @RequestBody VendorActionRequest actionRequest,
            HttpServletRequest request,
            Principal loggedInUser) {
        return vendorService.deleteVendor(actionRequest, request, loggedInUser);
    }
    @PostMapping(ApiConstant.VENDOR + ApiConstant.SLASH + ApiConstant.INCREASE_COMMISSION)
    @PreAuthorize("hasAuthority('EDIT_VENDOR')")
    public ApiResponse<?> increaseCommission(@RequestBody VendorCommissionRequest increaseCommissionRequest, HttpServletRequest request, Principal loggedInUser){
     return vendorService.increaseCommission(increaseCommissionRequest, request, loggedInUser);
    }

}
