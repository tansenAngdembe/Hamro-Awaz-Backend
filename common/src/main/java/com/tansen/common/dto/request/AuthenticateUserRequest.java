package com.tansen.common.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticateUserRequest  extends ModelBase {
    @NotBlank(message="Email||Phone is required")
    private String email;
    @NotBlank(message="Password is required")
    private String password;
}
