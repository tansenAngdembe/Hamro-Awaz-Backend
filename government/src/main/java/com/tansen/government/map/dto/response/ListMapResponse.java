package com.tansen.government.map.dto.response;

import com.tansen.government.map.dto.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ListMapResponse {
    private Double latitude;
    private Double longitude;
   private ComplaintDto  complaint;

}
