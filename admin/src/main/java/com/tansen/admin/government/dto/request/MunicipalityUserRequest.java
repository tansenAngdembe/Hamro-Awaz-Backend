package com.tansen.admin.government.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MunicipalityUserRequest extends ModelBase {
    @NotBlank(message = "Vendor User ID cannot be blank")
    private String uniqueId;
}
