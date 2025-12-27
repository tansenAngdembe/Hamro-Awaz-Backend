package com.tansen.app.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordRequest extends ModelBase {
    @Email
    @NotBlank(message = "Email is required")
    private String email;
}