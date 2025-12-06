package com.tansen.entity;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Table(name = "admins")
public class Admin extends AbstractEntity implements UserDetails {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "unique_id")
    private String uniqueId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Status status;

    @JoinColumn(name = "access_group", nullable = false, referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AccessGroup accessGroup;

    @Column(name = "password_changed_date")
    private LocalDateTime passwordChangedDate;

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

    @Column(name = "is_super_admin")
    private boolean isSuperAdmin;

    @Transactional
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Hibernate.initialize(this.accessGroup.getAccessGroupRoleMaps());
        return this.accessGroup
                .getAccessGroupRoleMaps()
                .stream()
                .filter(AccessGroupRoleMap::getIsActive)
                .map(AccessGroupRoleMap::getAdminRole)
                .map(roles -> new SimpleGrantedAuthority(roles.getPermission()))
                .collect(Collectors.toList());
    }
    @Override
    public boolean isAccountNonExpired() {return UserDetails.super.isAccountNonExpired();}
    @Override
    public boolean isAccountNonLocked() {return UserDetails.super.isAccountNonLocked();}
    @Override
    public boolean isCredentialsNonExpired() {return UserDetails.super.isCredentialsNonExpired();}
    @Override
    public boolean isEnabled() {return UserDetails.super.isEnabled();}


}
