package com.tansen.app.dto.request;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintCoordinatesRequest extends ModelBase {
    private Double latitude;
    private Double longitude;

}
