package com.tansen.administrative.accessGroup.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MunicipalityRolesResponse extends ModelBase {
    private String name;
    private String description;
    private String icon;
    private String navigation;
    private Integer position;
    private String uiGroupName;
    private MunicipalityRolesResponse parentRole;
    private String parentName;
    private String permission;
}
