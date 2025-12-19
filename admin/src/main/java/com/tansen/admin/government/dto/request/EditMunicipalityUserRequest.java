package com.tansen.admin.government.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMunicipalityUserRequest extends ModelBase {
    @NotBlank(message = "Unique ID is required")
    private String uniqueId;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "AccessGroup Name is required")
    private String authorityAccessGroupName;

}
