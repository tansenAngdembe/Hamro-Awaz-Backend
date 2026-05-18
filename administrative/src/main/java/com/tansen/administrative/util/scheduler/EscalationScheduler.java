package com.tansen.administrative.util.scheduler;

import com.tansen.common.dto.model.SendEmailRequest;
import com.tansen.common.service.MailService;
import com.tansen.entity.*;
import com.tansen.entity.enums.Priority;
import com.tansen.administrative.emaillog.mapper.AuthorityUserEmailLogMapper;
import com.tansen.repository.ComplaintRepository;
import com.tansen.repository.EscalationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Async
public class EscalationScheduler {
    private final ComplaintRepository complaintRepository;
    private final EscalationRepository escalationRepository;
    private final MailService mailService;
    private final AuthorityUserEmailLogMapper authorityUserEmailLogMapper;

    @Scheduled(fixedRate = 100000)
    public void escalate() {
        LocalDateTime now = LocalDateTime.now();
        List<Complaint> complaints = complaintRepository.findByPriorityNot(Priority.ESCALATED);
        List<Complaint> modified = new ArrayList<>();

        // ✅ Build a map keyed by BOTH category + municipality to avoid N+1 queries
        Map<String, Escalation> escalationMap = escalationRepository.findAllByActiveTrue()
                .stream()
                .collect(Collectors.toMap(
                        e -> escalationKey(e.getCategory(), e.getMunicipality()),
                        e -> e,
                        (existing, duplicate) -> existing // keep first if duplicates exist
                ));

        for (Complaint complaint : complaints) {
            // ✅ Lookup using both category AND municipality
            String key = escalationKey(complaint.getCategory(), complaint.getMunicipality());
            Escalation escalation = escalationMap.get(key);

            if (escalation == null) continue;

            long hoursPassed = Duration.between(complaint.getCreatedDate(), now).toHours();

            if (hoursPassed >= escalation.getMaxResolutionHours()) {
                complaint.setPriority(Priority.ESCALATED);
                complaint.setEscalatedAt(LocalDateTime.now());
                modified.add(complaint);

                if (complaint.getAssignedTo() != null) {
                    AuthorityEscalationEmailLog log = authorityUserEmailLogMapper
                            .mapEscalationEmail(complaint.getAssignedTo(), complaint);

                    SendEmailRequest sendEmailRequest = new SendEmailRequest();
                    sendEmailRequest.setReplyToEmail(complaint.getMunicipality().getEmail());
                    sendEmailRequest.setRecipient(complaint.getAssignedTo().getEmail());
                    sendEmailRequest.setSubject("Escalation Alert");
                    sendEmailRequest.setMessage(log.getMessage());
                    mailService.sendEmail(sendEmailRequest);
                }

            } else if (hoursPassed >= escalation.getEscalationTime()
                    && complaint.getPriority() != Priority.HIGH) {
                complaint.setPriority(Priority.HIGH);
                modified.add(complaint);
            }
        }

        if (!modified.isEmpty()) {
            complaintRepository.saveAll(modified);
        }
    }

    // ✅ Composite key helper using IDs
    private String escalationKey(Category category, AdministrativeUnit municipality) {
        return category.getId() + ":" + municipality.getId();
    }
}
