package com.tansen.government.slarules.mapper;

import com.tansen.common.constant.ComplaintStatusConstant;
import com.tansen.entity.*;
import com.tansen.government.complaints.dto.response.ListComplainsResponse;
import com.tansen.government.slarules.dto.CreateEscalationRequest;
import com.tansen.government.slarules.dto.ListEscalationResponse;
import com.tansen.repository.CategoryRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class EscalationMapper {


    public abstract ListEscalationResponse entityToResponse(Escalation escalation);

    public List<ListEscalationResponse> listEscalationResponses(List<Escalation> escalations) {
        return escalations.stream().map(this::entityToResponse).collect(Collectors.toList());
    }


    public Escalation createEscalationMap(CreateEscalationRequest createEscalationRequest, Municipality municipality, Category category) {
        Escalation escalation = new Escalation();

        escalation.setMaxResolutionHours(createEscalationRequest.getMaxResolutionHours());
        escalation.setEscalationTime(createEscalationRequest.getEscalationTime());
        escalation.setResponseTime(createEscalationRequest.getResponseTime());
        escalation.setActive(true);
        escalation.setRuleName(createEscalationRequest.getRuleName());
        escalation.setCreatedAt(LocalDateTime.now());

        escalation.setCategory(category);
        escalation.setMunicipality(municipality);
        return escalation;
    }
}
