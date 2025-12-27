package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_registration_email_logs")
public class UserRegistrationEmailLog extends AbstractEntity {
    @Column(name = "email")
    private String email;

    @Column(name = "unique_id", unique = true)
    private String uniqueId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user", referencedColumnName = "id")
    private User user;

    @Column(name = "message")
    private String message;

    @Column(name = "is_sent")
    private Boolean isSent;

    @Column(name = "otp")
    private String otp;

    @Column(name = "is_otp_expired")
    private Boolean isOtpExpired;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;


}
