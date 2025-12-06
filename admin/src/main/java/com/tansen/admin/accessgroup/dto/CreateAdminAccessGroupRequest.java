package com.tansen.admin.accessgroup.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateAdminAccessGroupRequest extends ModelBase {
    private String name;
    private String description;
    private String remarks;
    private List<String> roleNames;
}
