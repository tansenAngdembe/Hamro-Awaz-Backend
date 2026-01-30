package com.tansen.government.accessGroup.controller;

import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.government.accessGroup.dto.request.CreateMunicipalityAccessGroupRequest;
import com.tansen.government.accessGroup.dto.EditMunicipalityAccessGroupRequest;
import com.tansen.government.accessGroup.dto.MunicipalityAccessGroupRequest;
import com.tansen.government.accessGroup.service.AccessGroupService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController("municipalityAccessGroupController")
@RequestMapping(ApiConstant.MUNICIPALITY_API + ApiConstant.SLASH + ApiConstant.accessGroup)
public class AccessGroupController {
    private final AccessGroupService accessGroupService;

    public AccessGroupController(AccessGroupService accessGroupService) {
        this.accessGroupService = accessGroupService;
    }

    @GetMapping(ApiConstant.LIST)
    public ApiResponse<?> listVendorAccessGroups(Principal loggedInUser) {
        return accessGroupService.listVendorAccessGroups(loggedInUser);
    }

    @PostMapping(ApiConstant.CREATE)
    public ApiResponse<?> createAccessGroup(@RequestBody @Valid CreateMunicipalityAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest) {
        return accessGroupService.createAccessGroup(request, loggedInUser,httpServletRequest);
    }

    @PostMapping(ApiConstant.UPDATE)
    public ApiResponse<?> updateAccessGroup(@RequestBody EditMunicipalityAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest) {
        return accessGroupService.editAccessGroup(request, loggedInUser, httpServletRequest);
    }

    @GetMapping(ApiConstant.LIST + ApiConstant.SLASH + ApiConstant.ACTIVE)
    public ApiResponse<?> listActive(Principal loggedInUser) {
        return accessGroupService.listActive(loggedInUser);
    }

    @PostMapping(ApiConstant.VIEW)
    public ApiResponse<?> viewActive(@RequestBody MunicipalityAccessGroupRequest request, Principal loggedInUser) {
        return accessGroupService.viewAccessGroup(request, loggedInUser);
    }

    @PostMapping(ApiConstant.DELETE)
    public ApiResponse<?> deleteAccessGroup(@RequestBody MunicipalityAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest) {
        return accessGroupService.deleteAccessGroup(request, loggedInUser, httpServletRequest);
    }


    @GetMapping(ApiConstant.ADMIN_ROLE + ApiConstant.SLASH + ApiConstant.LIST)
    public ApiResponse<?> listAdminRoles() {
        return accessGroupService.listAdminRoles();
    }
}
