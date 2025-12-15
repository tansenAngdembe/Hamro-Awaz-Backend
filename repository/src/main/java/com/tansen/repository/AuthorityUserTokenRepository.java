package com.tansen.repository;

import com.tansen.entity.AuthorityUserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityUserTokenRepository extends JpaRepository<AuthorityUserToken,Long> {
}
