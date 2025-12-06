package com.tansen.admin.admin.dto.request;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.request.AccessGroupDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAdminRequest extends ModelBase {
    @NotBlank(message = "Full name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
    @Pattern(regexp="^(97|98)[0-9]{8}$", message = "Invalid mobile number format")
    private String mobileNumber;

    @NotBlank(message = "Address is required")
    private String address;

    @Valid
    private AccessGroupDto accessGroup;
}
