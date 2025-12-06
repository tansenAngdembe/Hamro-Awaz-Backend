package com.tansen.admin.navigation.mapper;

import com.tansen.admin.role.dto.NavigationRoleResponse;
import com.tansen.admin.role.dto.RoleResponse;
import com.tansen.entity.AdminRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component
public class NavigationBuilder {
    private static final Logger LOG = LoggerFactory.getLogger(NavigationBuilder.class);

    private final Map<String, NavigationRoleResponse> navigationRoleResponses;
    private final Map<String, List<RoleResponse>> roleResponses;

    public NavigationBuilder() {
        navigationRoleResponses = new HashMap<>();
        roleResponses = new HashMap<>();
    }

    public List<NavigationRoleResponse> buildNavigation(List<AdminRole> roles) {
        try{
            navigationRoleResponses.clear();
            roleResponses.clear();
            buildRoleResponse(roles);
            buildNavigationResponse(roles);
            return  new ArrayList<>(navigationRoleResponses.values());
        }
        catch(Exception e){
            LOG.error("Exception at  buildNavigation :: {} ",e.getMessage());
            return null;
        }
    }
    private void buildRoleResponse(List<AdminRole> roles) {
        roles.forEach(role -> {
            RoleResponse roleResponse = setRoleResponse(new RoleResponse(), role);
            setChildRoleResponsesWithRespectToParentName(role, roleResponse).accept(roles);
        });
    }
    private RoleResponse setRoleResponse(RoleResponse roleResponse, AdminRole role) {
        roleResponse.setDescription(role.getDescription());
        roleResponse.setName(role.getName());
        roleResponse.setNavigation(role.getNavigation());
        roleResponse.setUiGroupName(role.getUiGroupName());
        roleResponse.setIcon(role.getIcon());
        roleResponse.setParentName(role.getParentName());
        roleResponse.setPosition(role.getPosition());
        roleResponse.setPermission(role.getPermission());
        return roleResponse;
    }
    private Consumer<List<AdminRole>> setChildRoleResponsesWithRespectToParentName(AdminRole role, RoleResponse roleResponse) {
        return roles -> {
            if (!isRoot(role.getName()) && !isRoot(role.getParentName())) {
                setChildResponse(role, roles).accept(roleResponse);
                if (!roleResponses.containsKey(role.getParentName())) {
                    List<RoleResponse> childRoles = new ArrayList<>();
                    roleResponses.put(role.getParentName(), childRoles);
                }

                roleResponses.get(role.getParentName()).add(roleResponse);
            }
        };

    }
    private boolean isRoot(String name) {
        return name.equalsIgnoreCase("ROOT");
    }

    private Consumer<RoleResponse> setChildResponse(AdminRole role, List<AdminRole> roles) {
        return roleResponse -> {
            List<RoleResponse> childRoleResponseList = new ArrayList<>();
            for (AdminRole childRole : roles) {
                if (role.getName().equalsIgnoreCase(childRole.getParentName())) {
                    RoleResponse childRoleResponse = setRoleResponse(new RoleResponse(), childRole);
                    childRoleResponseList.add(childRoleResponse);
                    setChildResponse(childRole, roles).accept(childRoleResponse);
                }
            }
            roleResponse.setChildRoles(childRoleResponseList);
        };
    }

    private void buildNavigationResponse(List<AdminRole> roles) {
        roles.forEach(this::buildForNavigationRoles);

    }

    private void buildForNavigationRoles(AdminRole role) {
        if (!isRoot(role.getName()) && isRoot(role.getParentName())) {
            if (navigationRoleResponses.containsKey(role.getName())) {
                navigationRoleResponses.get(role.getName()).setRoles(roleResponses.get(role.getName()));
            } else {
                NavigationRoleResponse navigationRoleResponse = setNavigationRole(new NavigationRoleResponse(), role);
                navigationRoleResponses.put(role.getName(), navigationRoleResponse);
            }
        }
    }
    private NavigationRoleResponse setNavigationRole(NavigationRoleResponse navigationRoleResponse, AdminRole role) {
        navigationRoleResponse.setUiGroupName(role.getDescription());
        navigationRoleResponse.setIcon(role.getIcon());
        navigationRoleResponse.setRoles(roleResponses.get(role.getName()));
        navigationRoleResponse.setPosition(role.getPosition());
        navigationRoleResponse.setName(role.getName());
        navigationRoleResponse.setDescription(role.getDescription());
        navigationRoleResponse.setNavigation(role.getNavigation());
        navigationRoleResponse.setPermission(role.getPermission());
        navigationRoleResponse.setDescription(role.getDescription());
        return navigationRoleResponse;
    }
}
