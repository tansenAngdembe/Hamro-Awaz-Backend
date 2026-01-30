package com.tansen.government.municipality.dto;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMunicipalityUserRequest extends ModelBase {

    @NotBlank(message = "Remarks is required.")
    private String remarks;

    @NotBlank(message = "Unique id is required")
    private String uniqueId;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "User name can only contain letters and space.")
    private String fullName;

    @NotBlank(message = "Mobile Number is required")
    @Pattern(regexp="^(97|98)[0-9]{8}$", message = "Invalid mobile number format")
    @Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @Valid
    private MunicipalityAccessGroupDto vendorAccessGroup;
}
