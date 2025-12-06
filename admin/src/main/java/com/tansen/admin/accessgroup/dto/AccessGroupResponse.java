package com.tansen.admin.accessgroup.dto;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.StatusDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class AccessGroupResponse extends ModelBase {
    private Long id;
    private String name;
    private String description;
    private StatusDto status;
    private Date createdAt;
    private Date updatedAt;
    private boolean isSuperAdminGroup;
    private String remarks;
    private List<AccessGroupRoleMapDto> accessGroupRoleMaps;

}
