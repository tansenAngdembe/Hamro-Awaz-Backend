package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "authority_users")
public class AuthorityUser extends AbstractEntity implements UserDetails{
    @ManyToOne(optional = false)
    @JoinColumn(name="municipality_id", referencedColumnName = "id")
    private Municipality municipality;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name ="email", nullable = false, unique = true)
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name="address")
    private String address;

    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Status status;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "password_changed_date")
    private LocalDateTime passwordChangeDate;

    @Column(name = "last_logged_in_time")
    private LocalDateTime lastLoggedInTime;

    @Column(name = "wrong_password_attempt_count")
    private Integer wrongPasswordAttemptCount;

    @Column(name = "profile_picture_name")
    private String profilePictureName;

    @Column(name = "otp_auth_secret")
    private String otpAuthSecret;

    @Column(name = "two_factor_enabled", nullable = false)
    private boolean twoFactorEnabled;

    @Column(name = "wrong_oto_auth_attempt_count")
    private Integer wrongOtpAuthAttemptCount;

    @Column(name = "is_authority_admin")
    private boolean isAuthorityAdmin;

    @JoinColumn(name = "authority_access_group_id", nullable = false, referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AuthorityAccessGroup  authorityAccessGroup;

    @Transactional
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
//        Hibernate is a Java framework that helps your application talk to a database.//
//            It removes the need to write SQL queries manually and converts Java objects into database rows automatically.This is called ORM (Object Relational Mapping).
        Hibernate.initialize(this.authorityAccessGroup.getAuthorityAccessGroupRoleMaps());
        return this.authorityAccessGroup
                .getAuthorityAccessGroupRoleMaps()
                .stream()
                .filter(AuthorityAccessGroupRoleMap::getIsActive)  // :: is called method reference operator work as lambda function
                .map(AuthorityAccessGroupRoleMap::getAuthorityUserRole)
                .map(roles -> new SimpleGrantedAuthority(roles.getPermission()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return "";
    }


    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

}
