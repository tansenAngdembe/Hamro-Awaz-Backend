package com.tansen.administrative.core.util;


import com.tansen.entity.AuthorityUser;
import com.tansen.entity.AuthorityUserToken;
import com.tansen.repository.AuthorityUserTokenRepository;

import java.util.Optional;
import java.util.function.Function;

public class AuthorityTokenUtil {
    public static AuthorityUserToken saveToken(AuthorityUser authorityUser, String accessToken, AuthorityUserTokenRepository tokenRepo, String refreshToken) {

        AuthorityUserToken token = new AuthorityUserToken();
        token.setAuthorityUser(authorityUser);
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        return tokenRepo.save(token);
    }

    public static void invalidateToken(String tokenValue, Function<String, Optional<AuthorityUserToken>> tokenFinder, AuthorityUserTokenRepository vendorTokenRepo) {
        tokenFinder.apply(tokenValue).ifPresent(token -> {
            token.setLoggedOut(true);
            vendorTokenRepo.save(token);
        });
    }
}
