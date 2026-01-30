package com.tansen.government.accessGroup.dto;


import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MunicipalityUserRoleResponse extends ModelBase {
    private String name;
    private String description;
    private String icon;
    private String uiGroupName;
    private String parentName;
}
