package com.tansen.government.complaints.dto.request;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintAssignRequest extends ModelBase {
    private String complaintId;
    private String authorityUserId;
}
