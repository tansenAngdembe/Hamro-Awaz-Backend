package com.tansen.admin.administrativelevel.dto;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListDistrictRequest extends ModelBase {
    @NotNull(message = "Province id is required")
    private Long provinceId;
}
