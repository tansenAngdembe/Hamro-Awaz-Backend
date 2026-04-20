package com.tansen.government.documentverification.dto;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.MunicipalityDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDocumentResponseDto extends ModelBase {

    private String citizenshipCardFront;
    private String citizenshipCardBack;
    private String nationalIdentityNumber;

    private String userFullName;
    private String userUniqueId;

    private MunicipalityDto municipalityName;

    private Boolean isDocumentVerified;

    private DocumentVerificationStatusDto verificationStatus;

    private RejectionCategoryDto rejectionCategory;
    private String rejectionReason;

    private LocalDateTime createdAt;
}
