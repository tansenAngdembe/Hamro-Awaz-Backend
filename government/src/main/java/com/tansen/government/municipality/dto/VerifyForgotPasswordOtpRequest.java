package com.tansen.government.municipality.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyForgotPasswordOtpRequest extends ModelBase {
    private String email;
    private int otp;
}
