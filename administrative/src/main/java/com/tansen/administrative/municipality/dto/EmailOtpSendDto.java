package com.tansen.administrative.municipality.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class EmailOtpSendDto extends ModelBase {
    private String userFullName;
    private int otp;
    private Date expirationTime;
    private String templateName;
}
