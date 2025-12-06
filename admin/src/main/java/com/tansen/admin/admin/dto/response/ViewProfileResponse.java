package com.tansen.admin.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.StatusDto;
import com.tansen.common.dto.request.AccessGroupDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ViewProfileResponse extends ModelBase {
    private String name;
    private String email;
    private String mobileNumber;
    private String username;
    private String address;
    private String uniqueId;
    private AccessGroupDto accessGroup;
    private StatusDto status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss a")
    private LocalDateTime lastLoggedInTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss a")
    private LocalDateTime passwordChangeDate;
    private Integer wrongPasswordAttemptCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss a")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss a")
    private LocalDateTime updatedAt;

    private String profilePictureName;
}