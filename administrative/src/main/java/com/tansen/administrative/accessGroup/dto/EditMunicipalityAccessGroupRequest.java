package com.tansen.administrative.accessGroup.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EditMunicipalityAccessGroupRequest extends ModelBase {
    private Long id;
    private String name;
    private String description;
    private String remarks;
    private List<String> roleNames;

}
