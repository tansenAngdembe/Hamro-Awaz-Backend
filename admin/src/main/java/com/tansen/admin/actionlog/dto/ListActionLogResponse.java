package com.tansen.admin.actionlog.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ListActionLogResponse extends ModelBase {
    private String remarks;
    private String targetType;
    private Long targetId;
    private String actionType;
    private AdminModel actionBy;
    private String ipAddress;
    private Date actionDate;
}
