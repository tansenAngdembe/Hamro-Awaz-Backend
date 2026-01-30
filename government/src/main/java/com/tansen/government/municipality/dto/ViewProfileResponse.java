package com.tansen.government.municipality.dto;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.StatusDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewProfileResponse extends ModelBase {
    private String fullName;
    private String email;
    private String mobileNumber;
    private String username;
    private String address;
    private MunicipalityUserAccessGroupDto vendorAccessGroup;
    private StatusDto status;
}