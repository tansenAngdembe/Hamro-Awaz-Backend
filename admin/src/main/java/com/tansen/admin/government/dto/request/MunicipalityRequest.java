package com.tansen.admin.government.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MunicipalityRequest extends ModelBase {
    @NotBlank(message = "Unique id is required")
    private String uniqueId;
}
