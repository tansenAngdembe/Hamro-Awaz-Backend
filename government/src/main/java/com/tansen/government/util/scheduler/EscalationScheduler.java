package com.tansen.government.util.scheduler;

import com.tansen.common.dto.model.SendEmailRequest;
import com.tansen.common.service.MailService;
import com.tansen.entity.AuthorityEscalationEmailLog;
import com.tansen.entity.AuthorityUserEmailLog;
import com.tansen.entity.Complaint;
import com.tansen.entity.Escalation;
import com.tansen.entity.enums.Priority;
import com.tansen.government.emaillog.mapper.AuthorityUserEmailLogMapper;
import com.tansen.repository.ComplaintRepository;
import com.tansen.repository.EscalationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Async
public class EscalationScheduler {
    private final ComplaintRepository complaintRepository;
    private final EscalationRepository escalationRepository;
    private final MailService mailService;
    private final AuthorityUserEmailLogMapper authorityUserEmailLogMapper;

    @Scheduled(fixedRate = 500000)
    public void escalate() {
        LocalDateTime now = LocalDateTime.now();

        List<Complaint>  complaints = complaintRepository.findByPriorityNot(Priority.ESCALATED);
        for (Complaint complaint : complaints) {
            Escalation escalation = escalationRepository.findByCategoryAndActiveTrue(complaint.getCategory()).orElse(null);
            if (escalation == null) {
                continue;
            }
            long hoursPassed = Duration.between(complaint.getCreatedDate(),now).toHours();
            if(hoursPassed >= escalation.getMaxResolutionHours()) {
                complaint.setPriority(Priority.ESCALATED);
                AuthorityEscalationEmailLog authorityEscalationEmailLog = authorityUserEmailLogMapper.mapEscalationEmail(complaint.getAssignedTo(), complaint);

                SendEmailRequest sendEmailRequest = new SendEmailRequest();
                sendEmailRequest.setReplyToEmail(complaint.getMunicipality().getEmail());
                sendEmailRequest.setRecipient(complaint.getAssignedTo().getEmail());
                sendEmailRequest.setSubject("Escalation Alert");
                sendEmailRequest.setMessage(authorityEscalationEmailLog.getMessage());
                mailService.sendEmail(sendEmailRequest);

            }else if (hoursPassed >= escalation.getEscalationTime()){
                complaint.setPriority(Priority.HIGH);
            }
        }
        complaintRepository.saveAll(complaints);

    }
}
