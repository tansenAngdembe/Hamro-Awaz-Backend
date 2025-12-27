package com.tansen.repository;

import com.tansen.entity.User;
import com.tansen.entity.UserToken;
import org.apache.commons.lang3.concurrent.UncheckedFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByAccessTokenAndLoggedOutFalse(String accessToken);

    Optional<UserToken> findByAccessToken(String accessToken);
    Optional<UserToken> findByRefreshToken(String refreshToken);

    List<UserToken> findByUserAndLoggedOutFalse(User user);

    List<UserToken> findAllByUserAndLoggedOutFalse(User user);
}
