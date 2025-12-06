package com.tansen.admin.admin.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteAdminRequest extends ModelBase {
    @NotBlank(message = "Remarks is required")
    private String remarks;

    @NotBlank(message = "Unique id is required")
    private String uniqueId;

}
