package com.tansen.government.actionlog.dto;

import com.tansen.common.dto.ModelBase;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActionLogModel extends ModelBase {
    private String remarks;
    private String targetType;
    private Integer targetId;
    private String actionType;
    private String ipAddress;
}
