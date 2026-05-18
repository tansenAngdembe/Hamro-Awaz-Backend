package com.tansen.administrative.complaints.dto;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.MunicipalityDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCommentResponse extends ModelBase  {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private MunicipalityDto municipality;
}
