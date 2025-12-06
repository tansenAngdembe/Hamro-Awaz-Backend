package com.tansen.admin.accessgroup.controller;


import com.tansen.admin.accessgroup.dto.AdminAccessGroupRequest;
import com.tansen.admin.accessgroup.dto.CreateAdminAccessGroupRequest;
import com.tansen.admin.accessgroup.dto.EditAdminAccessGroupRequest;
import com.tansen.admin.accessgroup.service.AccessGroupService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.ADMIN_API + ApiConstant.SLASH + ApiConstant.accessGroup)
public class AccessGroupController {
    private final AccessGroupService accessGroupService;

    public AccessGroupController(AccessGroupService accessGroupService) {
        this.accessGroupService = accessGroupService;
    }

    @PostMapping(ApiConstant.CREATE)
    public ApiResponse<?> createAccessGroup(@RequestBody CreateAdminAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest) {
        return accessGroupService.createAdminAccessGroup(request, loggedInUser, httpServletRequest);
    }

    @PostMapping(ApiConstant.UPDATE)
    public ApiResponse<?> updateAccessGroup(@RequestBody EditAdminAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest) {
        return accessGroupService.editAdminAccessGroup(request, loggedInUser, httpServletRequest);
    }


    @GetMapping(ApiConstant.LIST + ApiConstant.SLASH + ApiConstant.ACTIVE)
    public ApiResponse<?> listActive() {
        return accessGroupService.listActive();
    }

    @PostMapping(ApiConstant.VIEW)
    public ApiResponse<?> viewActive(@RequestBody AdminAccessGroupRequest request) {
        return accessGroupService.viewAdminAccessGroup(request);
    }

    @PostMapping(ApiConstant.DELETE)
    public ApiResponse<?> deleteAccessGroup(@RequestBody AdminAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest) {
        return accessGroupService.deleteAdminAccessGroup(request, loggedInUser, httpServletRequest);
    }

    @GetMapping(ApiConstant.ADMIN_ROLE + ApiConstant.SLASH + ApiConstant.LIST)
    public ApiResponse<?> listAdminRoles() {
        return accessGroupService.listAdminRoles();
    }

}
