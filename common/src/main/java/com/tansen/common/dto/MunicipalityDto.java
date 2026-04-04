package com.tansen.common.dto;

import com.tansen.common.dto.response.DistrictResponse;
import com.tansen.common.dto.response.ProvinceResponse;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MunicipalityDto extends ModelBase {
    private ProvinceResponse province;
    private DistrictResponse district;
    private String code;
    private String uniqueId;

}
