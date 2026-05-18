package com.tansen.administrative.user.dto;

import com.tansen.common.dto.MunicipalityDto;
import com.tansen.common.dto.StatusDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListUserResponse {

    private Long id;
    private String fullName;
    private String uniqueId;
    private String email;
    private String phoneNumber;
    private String address;
    private String profilePictureLink;
    private Boolean isActive;
    private Boolean isUserVerified;
    private LocalDateTime registeredDate;
    private LocalDateTime lastLoggedInTime;
    private LocalDateTime updatedAt;
    private StatusDto status;
    private MunicipalityDto municipality;


}