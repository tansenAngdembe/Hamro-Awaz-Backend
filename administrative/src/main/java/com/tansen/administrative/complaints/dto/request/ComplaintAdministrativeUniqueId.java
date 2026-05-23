package com.tansen.administrative.complaints.dto.request;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintAdministrativeUniqueId extends ModelBase {
    private String complaintUniqueId;
    private String remarks;
}
