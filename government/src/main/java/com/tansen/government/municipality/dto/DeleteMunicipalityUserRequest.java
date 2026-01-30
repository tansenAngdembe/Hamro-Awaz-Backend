package com.tansen.government.municipality.dto;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteMunicipalityUserRequest extends ModelBase {
    @NotBlank(message = "remarks is required")
    private String remarks;
    @NotBlank(message="unique id is required")
    private String uniqueId;
}
