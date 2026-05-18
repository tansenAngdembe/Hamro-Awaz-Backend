package com.tansen.administrative.map.dto.request;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintAssignRequest extends ModelBase {
    private String complaintId;
    private String authorityUserId;
}
