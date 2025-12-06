package com.tansen.admin.accessgroup.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRolesResponse extends ModelBase {
    private String name;
    private String description;
    private String icon;
    private String navigation;
    private Integer position;
    private String uiGroupName;
    private AdminRolesResponse parentRole;
    private String parentName;
    private String permission;
}
