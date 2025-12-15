package com.tansen.admin.government.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class MunicipalityCommissionRequest extends ModelBase {
    @NotBlank(message = "Unique Id is required")
    private String uniqueId;
    @NotBlank(message = "Remarks is required")
    private String remarks;
    @NotNull(message = "Commission percent is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Commission percent must be positive")
    private BigDecimal commissionPercent;
}
