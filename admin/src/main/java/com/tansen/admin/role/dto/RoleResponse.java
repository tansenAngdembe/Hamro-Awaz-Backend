package com.tansen.admin.role.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleResponse extends ModelBase {
    private String name;
    private String description;
    private String uiGroupName;
    private String navigation;
    private String parentName;
    private String icon;
    private int position;
    private List<RoleResponse> childRoles;
    private String permission;
}
