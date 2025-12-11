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
@Table(name="authority_email_logs")
public class AuthorityEmailLog  extends AbstractEntity{
    @Column(name="email", nullable = false)
    private String email;

    @JoinColumn(name="authority_user", referencedColumnName = "id")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private AuthorityUser authorityUser;

    @Column(name="message", nullable = false,columnDefinition = "TEXT")
    private String message;

    @Column(name="is_sent")
    private Boolean isSent;

    @Column(name="is_expired")
    private Boolean isExpired;

    @Column(name="unique_id", nullable = false)
    private String uniqueId;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;
}
