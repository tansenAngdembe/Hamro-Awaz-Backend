package com.tansen.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="authority_escalation_email_logs")
public class AuthorityEscalationEmailLog extends AbstractEntity{

    @Column(name="complaint_title", nullable = false)
    private String complaintTitle;

    @Column(name="category", nullable = false)
    private String category;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name="complaint_rule", nullable = false)
    private String complaintRule;

    @JoinColumn(name="assigned_to", referencedColumnName = "id")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private AuthorityUser assignedTo;

    @Column(name = "escalation_at",nullable = false)
    private LocalDateTime escalationAt;

    @Column(name="message", nullable = false)
    private String message;

    @Column(name="unique_id", nullable = false)
    private String uniqueId;


    @Column(name="meta_created_at", nullable = false)
    private LocalDateTime metaCreatedAt;
}
