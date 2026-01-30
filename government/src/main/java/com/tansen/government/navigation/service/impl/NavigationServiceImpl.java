package com.tansen.government.navigation.service.impl;


import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.common.exception.InvalidInputException;
import com.tansen.entity.AuthorityAccessGroup;
import com.tansen.entity.AuthorityUserRole;
import com.tansen.government.core.security.JwtToken;
import com.tansen.government.navigation.dto.NavigationRoleResponse;
import com.tansen.government.navigation.mapper.NavigationBuilder;
import com.tansen.government.navigation.service.NavigationService;
import com.tansen.repository.AuthorityAccessGroupRepository;
import com.tansen.repository.AuthorityAccessGroupRoleMapRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavigationServiceImpl implements NavigationService {
    private final JwtToken jwtToken;
    private final AuthorityAccessGroupRepository accessGroupRepository;
    private final AuthorityAccessGroupRoleMapRepository accessGroupRoleMapRepository;
    private final NavigationBuilder navigationBuilder;

    public NavigationServiceImpl(JwtToken jwtToken, AuthorityAccessGroupRepository accessGroupRepository, AuthorityAccessGroupRoleMapRepository accessGroupRoleMapRepository, NavigationBuilder navigationBuilder) {
        this.jwtToken = jwtToken;
        this.accessGroupRepository = accessGroupRepository;
        this.accessGroupRoleMapRepository = accessGroupRoleMapRepository;
        this.navigationBuilder = navigationBuilder;
    }

    @Override
    public ApiResponse<?> getAllNavigation() {
        String groupTypeName = jwtToken.getGroupTypeName();
        AuthorityAccessGroup accessGroup = accessGroupRepository.findByName(groupTypeName).orElseThrow(() -> new InvalidInputException("Access Group not found."));
        List<AuthorityUserRole> rolesList = accessGroupRoleMapRepository.getRolesByAccessGroup(accessGroup.getId());
        List<NavigationRoleResponse> navigationRoleResponseDtoList = getNavigationRoleResponse(rolesList);
        return ResponseUtil.getSuccessfulApiResponse(navigationRoleResponseDtoList, "Navigation fetched successfully.");
    }
    private List<NavigationRoleResponse> getNavigationRoleResponse(List<AuthorityUserRole> rolesList) {
        return navigationBuilder.buildNavigation(rolesList);
    }
}
