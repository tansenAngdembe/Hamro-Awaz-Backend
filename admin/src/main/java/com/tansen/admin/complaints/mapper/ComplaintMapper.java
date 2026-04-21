package com.tansen.admin.complaints.mapper;

import com.tansen.admin.complaints.dto.response.ComplaintResponse;
import com.tansen.admin.complaints.dto.response.ListComplainsResponse;
import com.tansen.entity.Complaint;
import com.tansen.repository.AuthorityUserEmailLogRepository;
import com.tansen.repository.ComplainStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ComplaintMapper {
    @Autowired
    private   ComplainStatusRepository complainStatusRepository;
//    @Autowired
//    private EmailContentUtil emailContentUtil;
    @Autowired
    private AuthorityUserEmailLogRepository authorityEmailLogRepository;




    public abstract ListComplainsResponse entityToResponse(Complaint complaint);
    public List<ListComplainsResponse> listComplainsResponses(List<Complaint> actionLog) {
        return actionLog.stream().map(this::entityToResponse).collect(Collectors.toList());
    }

    public abstract ComplaintResponse entityToComplaintResponse(Complaint complaint);


}
