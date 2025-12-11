package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "authority_user_tokens")
public class AuthorityUserToken extends AbstractEntity{

    @Column(name = "access_token",columnDefinition = "TEXT")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "logged_out")
    private boolean loggedOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_user_id", referencedColumnName = "id")
    private AuthorityUser authorityUser;
}