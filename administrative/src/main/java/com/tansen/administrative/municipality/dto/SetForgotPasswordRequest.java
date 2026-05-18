package com.tansen.administrative.municipality.dto;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetForgotPasswordRequest extends ModelBase {
    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;
    @NotNull(message = "OTP cannot be null")
    private int otp;
    @NotNull(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
    @NotNull(message = "Confirm Password cannot be null")
    private String confirmPassword;
}
