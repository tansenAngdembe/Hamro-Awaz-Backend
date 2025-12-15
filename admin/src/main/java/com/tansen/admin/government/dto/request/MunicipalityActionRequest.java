package com.tansen.admin.government.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MunicipalityActionRequest extends ModelBase {
    @NotBlank(message = "Unique Id is required")
    private String uniqueId;
    @NotBlank(message = "Remarks is required")
    private String remarks;
}
