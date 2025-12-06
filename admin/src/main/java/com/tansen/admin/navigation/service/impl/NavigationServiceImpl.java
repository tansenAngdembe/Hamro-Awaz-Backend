package com.tansen.admin.navigation.service.impl;


import com.tansen.admin.navigation.mapper.NavigationBuilder;
import com.tansen.admin.navigation.service.NavigationService;
import com.tansen.admin.role.dto.NavigationRoleResponse;
import com.tansen.admin.security.JwtToken;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.common.exception.InvalidInputException;
import com.tansen.entity.AccessGroup;
import com.tansen.entity.AdminRole;
import com.tansen.repository.AccessGroupRepository;
import com.tansen.repository.AccessGroupRoleMapRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavigationServiceImpl implements NavigationService {
    private final com.tansen.admin.security.JwtToken jwtToken;
    private final AccessGroupRepository accessGroupRepository;
    private final AccessGroupRoleMapRepository accessGroupRoleMapRepository;
    private final NavigationBuilder navigationBuilder;

    public NavigationServiceImpl(JwtToken jwtToken, AccessGroupRepository accessGroupRepository, AccessGroupRoleMapRepository accessGroupRoleMapRepository, NavigationBuilder navigationBuilder) {
        this.jwtToken = jwtToken;
        this.accessGroupRepository = accessGroupRepository;
        this.accessGroupRoleMapRepository = accessGroupRoleMapRepository;
        this.navigationBuilder = navigationBuilder;
    }

    @Override
    public ApiResponse<?> getAllNavigation() {
        String groupTypeName = jwtToken.getGroupTypeName();
        AccessGroup accessGroup = accessGroupRepository.findByName(groupTypeName).orElseThrow(() -> new InvalidInputException("Access Group not found."));
        List<AdminRole> rolesList = accessGroupRoleMapRepository.getRolesByAccessGroup(accessGroup.getId());
        List<NavigationRoleResponse> navigationRoleResponseDtoList = getNavigationRoleResponse(rolesList);
        return ResponseUtil.getSuccessfulApiResponse(navigationRoleResponseDtoList, "Navigation fetched successfully.");
    }
    private List<NavigationRoleResponse> getNavigationRoleResponse(List<AdminRole> rolesList) {
        return navigationBuilder.buildNavigation(rolesList);
    }
}
