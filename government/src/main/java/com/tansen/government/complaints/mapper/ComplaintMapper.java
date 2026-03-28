package com.tansen.government.complaints.mapper;

import com.tansen.common.constant.ComplaintStatusConstant;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.Complaint;
import com.tansen.government.complaints.dto.response.ListComplainsResponse;
import com.tansen.government.municipality.dto.AssignToListResponse;
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


    public abstract ListComplainsResponse entityToResponse(Complaint complaint);
    public List<ListComplainsResponse> listComplainsResponses(List<Complaint> actionLog) {
        return actionLog.stream().map(this::entityToResponse).collect(Collectors.toList());
    }

    public abstract AssignToListResponse entityToAssignUser(AuthorityUser authorityUser);
    public List<AssignToListResponse> listAllAssignTo(List<AuthorityUser> authorityUsers) {
        return authorityUsers.stream().map(this::entityToAssignUser).collect(Collectors.toList());
    }

  public Complaint assignedTo(Complaint complaint, AuthorityUser authorityUser) {
      complaint.setAssignedTo(authorityUser);
      complaint.setStatus(complainStatusRepository.findByName(ComplaintStatusConstant.ASSIGNED.getName()));
      return complaint;
  }
}
