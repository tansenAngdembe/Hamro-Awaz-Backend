package com.tansen.admin.accessgroup.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListAccessGroupResponse extends ModelBase {
    private Long id;
    private String name;
}
