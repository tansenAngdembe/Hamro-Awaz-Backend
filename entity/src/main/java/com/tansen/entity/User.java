package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="users")
public class User extends AbstractEntity {
    @Column(name="full_name", nullable = false)
    private String fullName;

    @Column(name="unique_id", nullable = false)
    private String uniqueId;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="password")
    private String password;

    @Column(name = "address")
    private String address;

    @Column(name = "registered_date")
    private LocalDateTime registeredDate;

    @Column(name = "password_changed_date")
    private LocalDateTime passwordChangeDate;

    @UpdateTimestamp
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    @Column(name = "last_logged_in_time")
    private LocalDateTime lastLoggedInTime;

    @Column(name = "is_active")
    private Boolean isActive;


    @Column(name = "wrong_password_attempt_count")
    private Integer wrongPasswordAttemptCount;

    @Column(name = "profile_picture_link")
    private String profilePictureLink;

    @JoinColumn(name="account_status", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Status status;

    @JoinColumn(name="role", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id")
    private Municipality municipality;


//    ---- validation check
    @Column(name = "is_user_verified", nullable = false)
    private Boolean isUserVerified = false;



}

