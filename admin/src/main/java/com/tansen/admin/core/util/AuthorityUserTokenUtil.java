package com.tansen.admin.core.util;

import com.tansen.entity.AuthorityUser;
import com.tansen.entity.AuthorityUserToken;
import com.tansen.repository.AuthorityUserTokenRepository;

import java.util.Optional;
import java.util.function.Function;

public class AuthorityUserTokenUtil {
        public static AuthorityUserToken saveToken(AuthorityUser authorityUser, String accessToken, String refreshToken) {
            AuthorityUserToken token = new AuthorityUserToken();
            token.setAuthorityUser(authorityUser);
            token.setAccessToken(accessToken);
            token.setRefreshToken(refreshToken);
            token.setLoggedOut(false);
            return token;
        }

    public static void invalidateToken(String tokenValue, Function<String, Optional<AuthorityUserToken>> tokenFinder, AuthorityUserTokenRepository authorityUserTokenRepository) {
            tokenFinder.apply(tokenValue).ifPresent(token -> {
                token.setLoggedOut(true);
                authorityUserTokenRepository.save(token);
            });
        }
}
