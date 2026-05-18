package com.tansen.administrative.map.mapper;

import com.tansen.entity.ComplaintCoordinates;
import com.tansen.administrative.map.dto.response.ListMapResponse;
import com.tansen.repository.ComplainStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class MapMapper {
    @Autowired
    private   ComplainStatusRepository complainStatusRepository;


    public abstract ListMapResponse entityToResponse(ComplaintCoordinates complaint);
    public List<ListMapResponse> listComplainsResponses(List<ComplaintCoordinates> actionLog) {
        return actionLog.stream().map(this::entityToResponse).collect(Collectors.toList());
    }






}
