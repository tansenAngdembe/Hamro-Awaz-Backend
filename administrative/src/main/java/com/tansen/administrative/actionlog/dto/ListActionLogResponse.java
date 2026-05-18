package com.tansen.administrative.actionlog.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ListActionLogResponse extends ModelBase {
    private String remarks;
    private String targetType;
    private Integer targetId;
    private String actionType;
    private String ipAddress;
    private MunicipalityModel actionBy;
    private Date actionDate;
}
