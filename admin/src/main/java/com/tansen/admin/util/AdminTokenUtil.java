package com.tansen.admin.util;


import com.tansen.entity.Admin;
import com.tansen.entity.AdminToken;
import com.tansen.repository.AdminTokenRepository;

import java.util.Optional;
import java.util.function.Function;

public class AdminTokenUtil {
    public static AdminToken saveToken(Admin admin, String accessToken, AdminTokenRepository tokenRepo, String refreshToken) {
        AdminToken token = new AdminToken();
        token.setAdmin(admin);
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        return tokenRepo.save(token);
    }
    /**
     * Example usage:
     * invalidateToken(refreshToken, adminTokenRepo::findByRefreshToken, adminTokenRepo);
     * invalidateToken(accessToken, adminTokenRepo::findByAccessToken, adminTokenRepo);
     */
    public static void invalidateToken(String tokenValue, Function<String, Optional<AdminToken>> tokenFinder, AdminTokenRepository adminTokenRepo) {
        tokenFinder.apply(tokenValue).ifPresent(token -> {
            token.setLoggedOut(true);
            adminTokenRepo.save(token);
        });
    }
}
