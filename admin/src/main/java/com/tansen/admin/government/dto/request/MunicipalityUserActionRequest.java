package com.tansen.admin.government.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MunicipalityUserActionRequest extends ModelBase {
    @NotBlank(message = "Unique ID is required")
    private String uniqueId;
    @NotBlank(message = "Remark is required")
    private String remarks;
}
