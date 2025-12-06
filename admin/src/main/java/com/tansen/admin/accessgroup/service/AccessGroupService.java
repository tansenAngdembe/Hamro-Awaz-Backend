package com.tansen.admin.accessgroup.service;


import com.tansen.admin.accessgroup.dto.AdminAccessGroupRequest;
import com.tansen.admin.accessgroup.dto.CreateAdminAccessGroupRequest;
import com.tansen.admin.accessgroup.dto.EditAdminAccessGroupRequest;
import com.tansen.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;

public interface AccessGroupService {
    ApiResponse<?> createAdminAccessGroup(CreateAdminAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest);
    ApiResponse<?> editAdminAccessGroup(EditAdminAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest);
    ApiResponse<?> viewAdminAccessGroup(AdminAccessGroupRequest request);
    ApiResponse<?> deleteAdminAccessGroup(AdminAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest);
    ApiResponse<?> listActive();
    ApiResponse<?> listAdminRoles();
}
