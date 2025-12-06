package com.tansen.admin.admin.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewAdminDetailRequest extends ModelBase {
    @NotBlank(message = "Unique id is required")
    private String uniqueId;
}
