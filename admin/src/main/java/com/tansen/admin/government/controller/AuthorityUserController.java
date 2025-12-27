package com.tansen.admin.government.controller;


import com.tansen.admin.government.dto.request.CreateMunicipalityUserRequest;
import com.tansen.admin.government.dto.request.EditMunicipalityUserRequest;
import com.tansen.admin.government.dto.request.MunicipalityUserActionRequest;
import com.tansen.admin.government.dto.request.MunicipalityUserRequest;
import com.tansen.admin.government.service.AuthorityUserService;
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
@RequestMapping(ApiConstant.ADMIN_API + ApiConstant.SLASH + ApiConstant.AUTHORITY_USER )
public class AuthorityUserController {
    private final AuthorityUserService authorityUserService;

    public AuthorityUserController(AuthorityUserService authorityUserService) {
        this.authorityUserService = authorityUserService;
    }

    @PostMapping(ApiConstant.CREATE)
    @PreAuthorize("hasAuthority('CREATE_AUTHORITY')")
    public ApiResponse<?> createAuthorityUser(
            @Valid @RequestPart(value = "authorityUser") CreateMunicipalityUserRequest createMunicipalityUserRequest,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            Principal loggedInUser,
            HttpServletRequest request) throws IOException {
        return authorityUserService.createAuthorityUser(createMunicipalityUserRequest, profilePicture, loggedInUser, request);
    }

    @PostMapping(ApiConstant.UPDATE)
    @PreAuthorize("hasAuthority('EDIT_AUTHORITY')")
    public ApiResponse<?> editAuthorityUser(
            @Valid @RequestPart(value = "authorityUser") EditMunicipalityUserRequest editMunicipalityUserRequest,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            Principal loggedInUser,
            HttpServletRequest request) throws IOException {
        return authorityUserService.editAuthorityUser(editMunicipalityUserRequest, profilePicture, loggedInUser, request);
    }

    @PostMapping(ApiConstant.LIST)
    @PreAuthorize("hasAuthority('VIEW_ALL_AUTHORITY')")
    public ApiResponse<?> getAuthorityUserList(@RequestBody SearchParam searchParam) {
        return authorityUserService.getAuthorityUserList(searchParam);
    }

    @PostMapping(ApiConstant.VIEW)
    @PreAuthorize("hasAuthority('VIEW_ALL_AUTHORITY')")
    public ApiResponse<?> viewAuthorityUser(@Valid @RequestBody MunicipalityUserRequest request) {
        return authorityUserService.viewAuthorityUser(request);
    }

    @PostMapping(ApiConstant.BLOCK)
    @PreAuthorize("hasAuthority('BLOCK_AUTHORITY')")
    public ApiResponse<?> blockAuthorityUser(@Valid @RequestBody MunicipalityUserActionRequest municipalityUserActionRequest, Principal loggedInUser, HttpServletRequest request) {
        return authorityUserService.blockAuthorityUser(municipalityUserActionRequest, loggedInUser, request);
    }

    @PostMapping(ApiConstant.UNBLOCK)
    @PreAuthorize("hasAuthority('UNBLOCK_AUTHORITY')")
    public ApiResponse<?> unblockAuthorityUser(@Valid @RequestBody MunicipalityUserActionRequest municipalityUserActionRequest, Principal loggedInUser, HttpServletRequest request) {
        return authorityUserService.unblockAuthorityUser(municipalityUserActionRequest, loggedInUser, request);
    }

    @PostMapping(ApiConstant.DELETE)
    @PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
    public ApiResponse<?> deleteAuthorityUser(@Valid @RequestBody MunicipalityUserActionRequest municipalityUserActionRequest, Principal loggedInUser, HttpServletRequest request) {
        return authorityUserService.deleteAuthorityUser(municipalityUserActionRequest, loggedInUser, request);
    }
}
