package com.tansen.admin.user.dto;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.MunicipalityDto;
import com.tansen.common.dto.StatusDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ListUserResponse extends ModelBase {
    private String fullName;

    private String uniqueId;

    private String email;

    private String phoneNumber;

    private String address;

    private LocalDateTime registeredDate;

    private LocalDateTime passwordChangeDate;

    private LocalDateTime updatedAt;

    private LocalDateTime lastLoggedInTime;

    private Boolean isActive;


    private Integer wrongPasswordAttemptCount;


    private StatusDto status;

    private MunicipalityDto municipality;


    private Boolean isUserVerified;

}
