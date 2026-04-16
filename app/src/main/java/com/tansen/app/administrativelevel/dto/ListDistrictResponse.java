package com.tansen.app.administrativelevel.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ListDistrictResponse extends ModelBase {
    private Long id;
    private String districtName;
}
