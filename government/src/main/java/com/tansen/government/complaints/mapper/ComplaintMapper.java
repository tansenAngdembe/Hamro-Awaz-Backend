package com.tansen.government.complaints.mapper;

import com.tansen.common.constant.ComplaintStatusConstant;
import com.tansen.common.constant.EmailTemplateNameConstant;
import com.tansen.common.utility.UuidUtil;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.AuthorityUserEmailLog;
import com.tansen.entity.Complaint;
import com.tansen.government.complaints.dto.EmailEscalationDto;
import com.tansen.government.complaints.dto.response.ComplaintResponse;
import com.tansen.government.complaints.dto.response.ListComplainsResponse;
import com.tansen.government.core.util.EmailContentUtil;
import com.tansen.government.municipality.dto.AssignToListResponse;
import com.tansen.government.municipality.dto.EmailOtpSendDto;
import com.tansen.repository.AuthorityEmailLogRepository;
import com.tansen.repository.AuthorityUserEmailLogRepository;
import com.tansen.repository.ComplainStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ComplaintMapper {
    @Autowired
    private   ComplainStatusRepository complainStatusRepository;
    @Autowired
    private EmailContentUtil emailContentUtil;
    @Autowired
    private AuthorityUserEmailLogRepository authorityEmailLogRepository;




    public abstract ListComplainsResponse entityToResponse(Complaint complaint);
    public List<ListComplainsResponse> listComplainsResponses(List<Complaint> actionLog) {
        return actionLog.stream().map(this::entityToResponse).collect(Collectors.toList());
    }

    public abstract ComplaintResponse entityToComplaintResponse(Complaint complaint);

    public abstract AssignToListResponse entityToAssignUser(AuthorityUser authorityUser);
    public List<AssignToListResponse> listAllAssignTo(List<AuthorityUser> authorityUsers) {
        return authorityUsers.stream().map(this::entityToAssignUser).collect(Collectors.toList());
    }

  public Complaint assignedTo(Complaint complaint, AuthorityUser authorityUser) {
      String uuid = UuidUtil.generateUuid();

      complaint.setAssignedTo(authorityUser);
      complaint.setStatus(complainStatusRepository.findByName(ComplaintStatusConstant.ASSIGNED.getName()));


      return complaint;
  }
  public AuthorityUserEmailLog assignedEmailContent(AuthorityUser authorityUser, Complaint complaint) {
      String uuid = UuidUtil.generateUuid();

      EmailEscalationDto emailEscalationDto = new EmailEscalationDto();
      emailEscalationDto.setAssignedTo(authorityUser.getName());
      emailEscalationDto.setComplaintRule("TEST");
      emailEscalationDto.setComplaintTitle(complaint.getComplaintTitle());
      emailEscalationDto.setTemplateName(EmailTemplateNameConstant.ASSIGN_COMPLAINT_TO);
      emailEscalationDto.setCategory(complaint.getCategory().getCategoryName());
      emailEscalationDto.setCreatedDate(complaint.getCreatedDate());
      emailEscalationDto.setPriority(complaint.getPriority().getName());

      String emailContent = emailContentUtil.prepareAssignToEmailContent(emailEscalationDto);

      AuthorityUserEmailLog userEmailLog = new AuthorityUserEmailLog();
      userEmailLog.setEmail(authorityUser.getEmail());
      userEmailLog.setAuthorityUser(authorityUser);
      userEmailLog.setUniqueId(uuid);
      userEmailLog.setMessage(emailContent);
      userEmailLog.setIsExpired(true);
      userEmailLog.setCreatedAt(LocalDateTime.now());
      authorityEmailLogRepository.save(userEmailLog);
      return userEmailLog;
  }


}
