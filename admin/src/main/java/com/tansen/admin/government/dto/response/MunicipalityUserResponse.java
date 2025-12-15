package com.tansen.admin.government.dto.response;

import com.cosmotech.admin.accessgroup.dto.ListAccessGroupResponse;
import com.cosmotech.common.dto.ModelBase;
import com.cosmotech.common.dto.response.StatusResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MunicipalityUserResponse extends ModelBase {
    private Integer id;
    private String uniqueId;

    private String fullName;
    private String username;
    private String email;
    private String mobileNumber;
    private String address;

    private MunicipalityResponse vendor;

    private ListAccessGroupResponse vendorAccessGroup;

    private StatusResponse status;

    private boolean isActive;
    private boolean isVendorAdmin;

    private boolean twoFactorEnabled;

    private Date passwordChangeDate;
    private Date lastLoggedInTime;

    private Integer wrongPasswordAttemptCount;
    private Integer wrongOtpAuthAttemptCount;

    private String profilePictureName;
}
