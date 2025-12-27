package com.tansen.app.dto.response;


import com.cosmotech.common.dto.ModelBase;
import com.cosmotech.common.dto.response.DistrictResponse;
import com.cosmotech.common.dto.response.LocalLevelResponse;
import com.cosmotech.common.dto.response.ProvinceResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VendorResponse extends ModelBase {

    private String uniqueId;

    private String businessName;

    private String businessOwnerName;

    private String registrationNumber;

    private String logoUrl;

    private String description;

    private ProvinceResponse province;

    private DistrictResponse district;

    private LocalLevelResponse localLevel;

    private Integer wardNumber;

    private String latitude;

    private String longitude;

    private String address;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime openingTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime closingTime;
}

