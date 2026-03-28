package com.tansen.app.dto.request;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ComplaintCoordinatesRequest extends ModelBase {
    private BigDecimal latitude;
    private BigDecimal longitude;

}
