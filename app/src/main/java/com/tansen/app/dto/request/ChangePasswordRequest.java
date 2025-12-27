package com.tansen.app.dto.request;

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
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,15}$",
            message = "Invalid password format"
    )
    @NotBlank(message = "Password is required")
    private String password;
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,15}$",
            message = "Invalid password format"
    )
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
}
