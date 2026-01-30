package com.tansen.repository;

import com.tansen.entity.AuthorityUser;
import com.tansen.entity.AuthorityUserEmailLog;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorityUserEmailLogRepository extends JpaRepository<AuthorityUserEmailLog,Long> {
    Optional<AuthorityUserEmailLog> findByUniqueId(@NotBlank(message = "Unique token is missing") String uuid);

    List<AuthorityUserEmailLog> findAllByAuthorityUserAndIsExpiredFalse(AuthorityUser user);

}
