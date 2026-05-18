package com.tansen.admin.government.controller;

import com.tansen.admin.government.dto.request.CreateMunicipalityRequest;
import com.tansen.admin.government.dto.request.EditMunicipalityRequest;
import com.tansen.admin.government.dto.request.MunicipalityActionRequest;
import com.tansen.admin.government.dto.request.MunicipalityRequest;
import com.tansen.admin.government.service.MunicipalityService;
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
public class MunicipalityController {
    private final MunicipalityService municipalityService;

    public MunicipalityController(MunicipalityService vendorService) {
        this.municipalityService = vendorService;
    }

    @PostMapping(ApiConstant.ADMINISTRATIVE + ApiConstant.SLASH + ApiConstant.CREATE)
    @PreAuthorize("hasAuthority('CREATE_ADMINISTRATIVE')")
    public ApiResponse<?> createMunicipality(
            @Valid @RequestPart("municipality") CreateMunicipalityRequest request,
            @RequestPart(value = "documentFile",required = false) MultipartFile documentFile,
            Principal loggedInUser,
            HttpServletRequest httpServletRequest) throws IOException {
        return municipalityService.createMunicipality(request, documentFile, loggedInUser, httpServletRequest);
    }

    @PostMapping(ApiConstant.ADMINISTRATIVE + ApiConstant.SLASH + ApiConstant.UPDATE)
    @PreAuthorize("hasAuthority('EDIT_ADMINISTRATIVE')")
    public ApiResponse<?> editMunicipality(
            @Valid @RequestPart("municipality") EditMunicipalityRequest request,
            @RequestPart(value = "logoFile", required = false) MultipartFile logoFile,
            @RequestPart(value = "documentFile", required = false) MultipartFile documentFile,
            Principal loggedInUser,
            HttpServletRequest httpServletRequest) throws IOException {
        return municipalityService.editMunicipality(request, documentFile, loggedInUser, httpServletRequest);
    }

    @PostMapping(ApiConstant.ADMINISTRATIVE + ApiConstant.SLASH + ApiConstant.LIST)
    @PreAuthorize("hasAuthority('VIEW_ALL_ADMINISTRATIVE')")
    public ApiResponse<?> listAllMunicipality(@Valid @RequestBody SearchParam searchParam) {
        return municipalityService.getMunicipalityList(searchParam);
    }

    @PostMapping(ApiConstant.ADMINISTRATIVE + ApiConstant.SLASH + ApiConstant.VIEW)
    @PreAuthorize("hasAuthority('VIEW_ALL_ADMINISTRATIVE')")
    public ApiResponse<?> viewMunicipalityDetails(
            @Valid @RequestBody MunicipalityRequest request) {
        return municipalityService.viewMunicipalityDetails(request);
    }

    @PostMapping(ApiConstant.ADMINISTRATIVE + ApiConstant.SLASH + ApiConstant.BLOCK)
    @PreAuthorize("hasAuthority('BLOCK_ADMINISTRATIVE')")
    public ApiResponse<?> blockMunicipality(
            @Valid @RequestBody MunicipalityActionRequest actionRequest,
            HttpServletRequest request,
            Principal loggedInUser) {
        return municipalityService.blockMunicipality(actionRequest, request, loggedInUser);
    }

    @PostMapping(ApiConstant.ADMINISTRATIVE + ApiConstant.SLASH + ApiConstant.UNBLOCK)
    @PreAuthorize("hasAuthority('UNBLOCK_ADMINISTRATIVE')")
    public ApiResponse<?> unblockMunicipality(
            @Valid @RequestBody MunicipalityActionRequest actionRequest,
            HttpServletRequest request,
            Principal loggedInUser) {
        return municipalityService.unblockMunicipality(actionRequest, request, loggedInUser);
    }

    @PostMapping(ApiConstant.ADMINISTRATIVE + ApiConstant.SLASH + ApiConstant.DELETE)
    @PreAuthorize("hasAuthority('DELETE_ADMINISTRATIVE')")
    public ApiResponse<?> deleteMunicipality(
            @Valid @RequestBody MunicipalityActionRequest actionRequest,
            HttpServletRequest request,
            Principal loggedInUser) {
        return municipalityService.deleteMunicipality(actionRequest, request, loggedInUser);
    }

}
