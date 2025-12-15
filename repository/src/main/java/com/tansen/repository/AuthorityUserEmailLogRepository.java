package com.tansen.repository;

import com.tansen.entity.AuthorityUserEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityUserEmailLogRepository extends JpaRepository<AuthorityUserEmailLog,Long> {
}
