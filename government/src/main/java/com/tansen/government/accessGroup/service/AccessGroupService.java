package com.tansen.government.accessGroup.service;

import com.tansen.common.dto.ApiResponse;
import com.tansen.government.accessGroup.dto.EditMunicipalityAccessGroupRequest;
import com.tansen.government.accessGroup.dto.MunicipalityAccessGroupRequest;
import com.tansen.government.accessGroup.dto.request.CreateMunicipalityAccessGroupRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;

public interface AccessGroupService {
    ApiResponse<?> listVendorAccessGroups(Principal loggedInUser);
    ApiResponse<?> createAccessGroup(CreateMunicipalityAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest);
    ApiResponse<?> editAccessGroup(EditMunicipalityAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest);
    ApiResponse<?> viewAccessGroup(MunicipalityAccessGroupRequest request, Principal loggedInUser);
    ApiResponse<?> deleteAccessGroup(MunicipalityAccessGroupRequest request, Principal loggedInUser, HttpServletRequest httpServletRequest);
    ApiResponse<?> listActive(Principal loggedInUser);
    ApiResponse<?> listAdminRoles();

}
