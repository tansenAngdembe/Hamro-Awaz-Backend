package com.tansen.administrative.accessGroup.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.StatusDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class MunicipalityAccessGroupResponse extends ModelBase {
    private String name;
    private String description;
    private StatusDto status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEEE, dd MMMM yyyy, hh:mm a")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEEE, dd MMMM yyyy, hh:mm a")
    private Date updatedAt;
    private boolean isVendorAdminGroup;
    private String remarks;
    private List<MunicipalityAccessGroupRoleResponse> vendorAccessGroupRoleMaps;
}
