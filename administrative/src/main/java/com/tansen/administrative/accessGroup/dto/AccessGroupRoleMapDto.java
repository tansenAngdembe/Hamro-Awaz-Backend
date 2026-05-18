package com.tansen.administrative.accessGroup.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessGroupRoleMapDto extends ModelBase {
    private AccessGroupDto accessGroup;
    private Boolean isActive;
    private MunicipalityRolesResponse adminRole;
}
