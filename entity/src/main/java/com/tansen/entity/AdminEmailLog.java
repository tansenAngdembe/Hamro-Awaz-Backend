package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="admin_email_logs")
public class AdminEmailLog extends AbstractEntity {
    @Column(name="email", nullable = false)
    private String email;

    @JoinColumn(name="admin", referencedColumnName = "id")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Admin admin;

    @Column(name="message", nullable = false)
    private String message;

    @Column(name="is_sent")
    private Boolean isSent;

    @Column(name="is_expired")
    private Boolean isExpired;

    @Column(name="uuid", nullable = false)
    private String uuid;

    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;
}
