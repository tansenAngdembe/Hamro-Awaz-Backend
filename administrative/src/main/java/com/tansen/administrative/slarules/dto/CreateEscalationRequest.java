package com.tansen.administrative.slarules.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateEscalationRequest extends ModelBase {
    private int maxResolutionHours;
    private int escalationTime;
    private int responseTime;
    private String ruleName;
    private String categoryName;



}
