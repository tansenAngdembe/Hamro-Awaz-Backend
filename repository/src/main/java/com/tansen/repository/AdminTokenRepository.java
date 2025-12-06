package com.tansen.repository;

import com.tansen.entity.Admin;
import com.tansen.entity.AdminToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminTokenRepository extends JpaRepository<AdminToken, Long> {
    Optional<AdminToken> findByAccessToken(String accessToken);
    Optional<AdminToken> findByRefreshToken(String refreshToken);
    Optional<AdminToken> findByAccessTokenAndLoggedOutFalse(String token);
    Optional<AdminToken> findByRefreshTokenAndLoggedOutFalse(String token);
    List<AdminToken> findByAdminAndLoggedOutFalse(Admin existingAdmin);
}
