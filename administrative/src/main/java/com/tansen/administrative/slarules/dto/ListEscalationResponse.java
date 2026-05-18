package com.tansen.administrative.slarules.dto;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.response.CategoryResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ListEscalationResponse extends ModelBase {
    private int maxResolutionHours;
    private int escalationTime;
    private int responseTime;
    private Boolean active;
    private String ruleName;
    private LocalDateTime createdAt;
    private CategoryResponse category;
}
