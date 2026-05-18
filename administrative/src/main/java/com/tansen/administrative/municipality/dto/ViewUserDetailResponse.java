package com.tansen.administrative.municipality.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ViewUserDetailResponse extends ModelBase {

    private String fullName;

    private String username;

    private String uniqueId;

    private String email;

    private String mobileNumber;

    private String address;

    private boolean isActive;

    private Date passwordChangeDate;

    private Date lastLoggedInTime;

    private Integer wrongPasswordAttemptCount;

    private String profilePictureName;

    private String otpAuthSecret;

    private boolean twoFactorEnabled;

    private Integer wrongOtpAuthAttemptCount;

    private boolean isVendorAdmin;

    private MunicipalityAccessGroupDto vendorAccessGroup;

}
