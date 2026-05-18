package com.tansen.administrative.accessGroup.dto;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.StatusDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListAccessGroupResponse extends ModelBase {
    private Long id;
    private String name;
    private StatusDto status;
}
