package com.tansen.government.navigation.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NavigationRoleResponse extends ModelBase {
    private String name;
    private String description;
    private String uiGroupName;
    private String navigation;
    private String parentName;
    private String icon;
    private int position;
    private List<RoleResponse> roles;
    private String permission;
}
