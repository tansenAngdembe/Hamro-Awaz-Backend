package com.tansen.app.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class NearByComplaintRequest {
    BigDecimal latitude;
    BigDecimal longitude;
    Double radiusKm;
}
