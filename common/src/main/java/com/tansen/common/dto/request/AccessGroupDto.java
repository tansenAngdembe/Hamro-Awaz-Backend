package com.tansen.common.dto.request;

import com.tansen.entity.AccessGroup;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessGroupDto extends AccessGroup {
    private Long id;
    private String name;
}
