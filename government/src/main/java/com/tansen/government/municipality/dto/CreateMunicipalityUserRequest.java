package com.tansen.government.municipality.dto;


import com.tansen.common.dto.ModelBase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMunicipalityUserRequest extends ModelBase {
    @NotBlank(message = "User name is required")
    @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Full name can only contain letters and space.")
    private String fullName;

    @Email(message = "Please provide a valid email address.")
    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
    @Pattern(regexp="^(97|98)[0-9]{8}$", message = "Invalid mobile number format")
    private String mobileNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @Valid
    private MunicipalityAccessGroupDto vendorAccessGroup;


}

