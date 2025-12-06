package com.tansen.admin.actionlog.mapper;


import com.tansen.admin.actionlog.dto.ListActionLogResponse;
import com.tansen.entity.ActionLog;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class AdminActionLogMapper {
    public abstract ListActionLogResponse entityToResponse(ActionLog actionLog);
    public List<ListActionLogResponse> listActionLogRes(List<ActionLog> actionLog) {
        return actionLog.stream().map(this::entityToResponse).collect(Collectors.toList());
    }
}
