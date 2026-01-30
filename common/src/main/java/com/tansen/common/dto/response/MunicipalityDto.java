package com.tansen.common.dto.response;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MunicipalityDto extends ModelBase{
    private String governmentName;
    private String code;
    private String description;

}
