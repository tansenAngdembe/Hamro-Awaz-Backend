package com.tansen.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user_email_logs")
public class UserEmailLog extends AbstractEntity {
    @Column(name="email", nullable = false)
    private String email;

    @JoinColumn(name="user", referencedColumnName = "id")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private User user;

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
