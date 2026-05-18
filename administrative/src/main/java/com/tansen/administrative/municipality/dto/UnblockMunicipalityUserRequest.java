package com.tansen.administrative.municipality.dto;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnblockMunicipalityUserRequest extends ModelBase {
    @NotBlank(message="unique_id is required")
    private String uniqueId;
    @NotBlank(message="remark is required")
    private String remarks;
}
