package com.tansen.government.municipality.dto;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.StatusDto;
import com.tansen.common.dto.request.AccessGroupDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignToListResponse extends ModelBase {
    private String name;
    private String uniqueId;
    private AccessGroupDto authorityAccessGroup;
    private StatusDto  status;
}
