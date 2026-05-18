package com.tansen.app.dto.response;

import com.tansen.common.dto.ModelBase;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class NearByComplainsResponse extends ModelBase {
    private List<ListNearByComplainsResponse> listNearByComplainsResponse;
    private Long count;
}
