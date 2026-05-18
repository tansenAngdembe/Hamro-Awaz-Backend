package com.tansen.administrative.complaints.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EscalationDto extends ModelBase {
    private String ruleName;
    private int maxResolutionHours;
    private int escalationTime;
    private int responseTime;

}
