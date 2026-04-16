package com.tansen.app.administrativelevel.dto;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListLocalLevelRequest extends ModelBase {
    @NotNull(message = "District id is required")
    private Long districtId;
}
