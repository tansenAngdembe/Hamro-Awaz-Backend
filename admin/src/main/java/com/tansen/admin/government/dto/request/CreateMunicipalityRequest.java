package com.tansen.admin.government.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMunicipalityRequest extends ModelBase {
    @NotBlank(message = "GovernmentName name is required")
    private String governmentName;

    @NotBlank(message = "Email owner name is required")
    private String email;

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

    @NotBlank(message = "Latitude is required")
    private String latitude;

    @NotBlank(message = "Longitude is required")
    private String longitude;

    @NotBlank(message = "Address is required")
    private String address;


    @NotBlank(message = "Full name is required")
    private String authorityAdminFullName;


    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String authorityAdminEmail;

    @NotBlank(message = "Mobile number is required")
    private String authorityAdminPhoneNumber;

    @NotBlank(message = "Address is required")
    private String authorityAdminAddress;

}
