package com.tansen.admin.government.dto.response;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.response.DistrictResponse;
import com.tansen.common.dto.response.LocalLevelResponse;
import com.tansen.common.dto.response.ProvinceResponse;
import com.tansen.common.dto.response.StatusResponse;
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

    private String governmentName;

    private String email;

    private String code;

    private String description;

    private String logoUrl;

    private String documentUrl;


    private StatusResponse status;

    private ProvinceResponse province;

    private DistrictResponse district;

    private LocalLevelResponse localLevel;

    private Integer wardNumber;

    private String latitude;

    private String longitude;

    private String address;
}
