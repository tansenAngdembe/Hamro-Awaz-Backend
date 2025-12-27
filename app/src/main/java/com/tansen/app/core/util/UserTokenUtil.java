package com.tansen.app.core.util;


import com.tansen.entity.User;
import com.tansen.entity.UserToken;
import com.tansen.repository.UserTokenRepository;

import java.util.List;
import java.util.Optional;

public class UserTokenUtil {
    public static UserToken saveToken(User user, String accessToken, UserTokenRepository tokenRepo, String refreshToken) {
        UserToken token = new UserToken();
        token.setUser(user);
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        return tokenRepo.save(token);
    }
    public static void invalidateRefreshToken(String refreshToken, UserTokenRepository userTokenRepository) {
        Optional<UserToken> token = userTokenRepository.findByRefreshToken(refreshToken);
        if (token.isPresent()) {
            UserToken userToken = token.get();
            userToken.setLoggedOut(true);
            userTokenRepository.save(userToken);
        }
    }
    public static void invalidateToken(User user, UserTokenRepository userTokenRepository) {
        List<UserToken> tokenList = userTokenRepository.findByUserAndLoggedOutFalse(user);
        tokenList.forEach(userToken -> userToken.setLoggedOut(true));
            userTokenRepository.saveAll(tokenList);
    }

}
