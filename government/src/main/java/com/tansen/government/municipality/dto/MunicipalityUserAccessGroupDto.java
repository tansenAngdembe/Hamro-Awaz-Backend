package com.tansen.government.municipality.dto;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MunicipalityUserAccessGroupDto extends ModelBase {
    @NotBlank(message = "Access group name is required")
    private String name;
}
