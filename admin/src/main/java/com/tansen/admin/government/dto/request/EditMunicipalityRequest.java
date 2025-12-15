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

    @NotBlank(message = "Business name is required")
    private String businessName;

    @NotBlank(message = "Business owner name is required")
    private String businessOwnerName;

    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "PAN number is required")
    private String panNumber;

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

    @NotNull(message = "Opening time is required")
    private LocalTime openingTime;

    @NotNull(message = "Closing time is required")
    private LocalTime closingTime;

    @NotNull(message = "Commission percent is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Commission percent must be positive")
    private BigDecimal commissionPercent;
}
