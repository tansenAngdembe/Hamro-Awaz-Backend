package com.tansen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "forgot_password_otp")
public class ForgotPasswordOtp extends AbstractEntity {
    @Column(name = "email")
    private String email;

    @Column(name = "otp", nullable = false)
    private int otp;

    @Column(name = "user_unique_id")
    private String userUniqueId;

    @Column(name = "expiration_time", nullable = false)
    private Instant validDate;

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "created_at")
    private Instant createdAt;
}
