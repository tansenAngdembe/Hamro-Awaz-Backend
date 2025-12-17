package com.tansen.admin.administrativelevel.dto;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListWardRequest extends ModelBase {
    @NotNull(message = "Local level id is required")
    private Long localLevelId;
}
