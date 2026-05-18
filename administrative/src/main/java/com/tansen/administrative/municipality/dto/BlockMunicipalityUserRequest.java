package com.tansen.administrative.municipality.dto;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockMunicipalityUserRequest extends ModelBase {
    @NotBlank(message = "Remarks is required.")
    private String remarks;

    @NotBlank(message = "Remarks is required.")
    private String uniqueId;
}
