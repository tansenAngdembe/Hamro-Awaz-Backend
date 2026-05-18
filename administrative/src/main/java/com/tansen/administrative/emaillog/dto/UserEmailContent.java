package com.tansen.administrative.emaillog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserEmailContent {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email template is required")
    private String template;
    @NotNull(message="Expiration time is required")
    private Date expirationTime;
    @NotBlank(message = "UUID cannot be null")
    private String uuid;
}
