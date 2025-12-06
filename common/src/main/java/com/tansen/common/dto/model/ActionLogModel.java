package com.tansen.common.dto.model;

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
    private Long targetId;
    private String actionType;
    private String ipAddress;
}
