package com.tansen.admin.government.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditMunicipalityRequest extends ModelBase {
    @NotBlank(message = "Unique ID is required")
    private String uniqueId;

    @NotBlank(message = "GovernmentName name is required")
    private String governmentName;

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Province ID is required")
    private Integer provinceId;

    @NotNull(message = "District ID is required")
    private Integer districtId;

    @NotNull(message = "Local level ID is required")
    private Integer localLevelId;

    @NotNull(message = "Ward number is required")
    @Min(value = 1, message = "Ward number must be at least 1")
    private Integer wardNumber;

    @NotBlank(message = "Latitude is required")
    private String latitude;

    @NotBlank(message = "Longitude is required")
    private String longitude;

    @NotBlank(message = "Address is required")
    private String address;
}
