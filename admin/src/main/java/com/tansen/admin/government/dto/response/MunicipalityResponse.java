package com.tansen.admin.government.dto.response;

import com.tansen.common.dto.ModelBase;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MunicipalityResponse extends ModelBase {

    private String uniqueId;

    private String businessName;

    private String businessOwnerName;

    private String registrationNumber;

    private String panNumber;

    private String logoUrl;

    private String documentUrl;

    private String description;

    private StatusResponse status;

    private ProvinceResponse province;

    private DistrictResponse district;

    private LocalLevelResponse localLevel;

    private Integer wardNumber;

    private String latitude;

    private String longitude;

    private String address;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private BigDecimal commissionPercent;

    private boolean verifiedByAdmin;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
