package com.tansen.admin.government.dto.response;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.StatusDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuthorityUserResponse extends ModelBase {
    private String name;
    private String uniqueId;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoggedInTime;
    private LocalDateTime passwordChangeDate;
    private String profilePictureName;
    private boolean isAuthorityAdmin;
    private StatusDto status;

}
