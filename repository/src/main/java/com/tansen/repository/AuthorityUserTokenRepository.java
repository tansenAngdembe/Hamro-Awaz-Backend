package com.tansen.repository;

import com.tansen.entity.AuthorityUser;
import com.tansen.entity.AuthorityUserToken;
import org.apache.commons.lang3.concurrent.UncheckedFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorityUserTokenRepository extends JpaRepository<AuthorityUserToken,Long> {
    List<AuthorityUserToken> findByAuthorityUserAndLoggedOutFalse(AuthorityUser authorityUser);

    Optional<AuthorityUserToken> findByRefreshToken(String refreshToken);

    Optional<AuthorityUserToken> findByAccessTokenAndLoggedOutFalse(String token);

}
