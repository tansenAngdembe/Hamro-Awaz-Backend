package com.tansen.government.municipality;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.StatusDto;
import com.tansen.government.municipality.dto.AuthorityAccessGroupDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ViewAuthorityProfileRequest extends ModelBase {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private Boolean isActive;
    private StatusDto status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime passwordChangeDate;
    private LocalDateTime lastLoggedInTime;
    private AuthorityAccessGroupDto authorityAccessGroup;

}
