package com.tansen.administrative.actionlog.mapper;


import com.tansen.entity.AuthorityUserActionLog;
import com.tansen.administrative.actionlog.dto.ListActionLogResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class MunicipalityActionLogMapper {

    public abstract ListActionLogResponse entityToResponse(AuthorityUserActionLog vendorUserActionLog);
    public List<ListActionLogResponse> listActionLogRes(List<AuthorityUserActionLog> vendorUserActionLogs) {
        return vendorUserActionLogs.stream().map(this::entityToResponse).collect(Collectors.toList());
    }

}
