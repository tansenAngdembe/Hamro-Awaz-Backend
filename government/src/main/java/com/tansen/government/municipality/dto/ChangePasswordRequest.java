package com.tansen.government.municipality.dto;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest extends ModelBase {
    @NotBlank(message = "Old password is required")
    private String oldPassword;
    @Pattern(
            regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*\\d)(?=.*[a-z]).{8,15}$",
            message = "Invalid password format"
    )
    @NotBlank(message = "Password is required")
    private String password;
    @Pattern(
            regexp = "^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*\\d)(?=.*[a-z]).{8,15}$",
            message = "Invalid password format"
    )
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}