package com.tansen.repository;

import com.tansen.entity.AuthorityUserActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityUserActionLogRepository extends JpaRepository<AuthorityUserActionLog,Long> {
}
