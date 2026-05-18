package com.tansen.administrative.map.dto.response;

import com.tansen.administrative.map.dto.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListMapResponse {
    private Double latitude;
    private Double longitude;
   private ComplaintDto  complaint;

}
