package com.tansen.administrative.accessGroup.dto;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessGroupDto extends ModelBase {
    private Long id;
    @NotBlank(message = "Access group name is required")
    private String name;
}
