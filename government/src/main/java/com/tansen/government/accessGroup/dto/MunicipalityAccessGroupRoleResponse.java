package com.tansen.government.accessGroup.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MunicipalityAccessGroupRoleResponse extends ModelBase {
    private Boolean isActive;
    private MunicipalityUserRoleResponse vendorUserRole;
}
