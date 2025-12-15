package com.tansen.repository;

import com.tansen.entity.AuthorityEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityEmailLogRepository extends JpaRepository<AuthorityEmailLog,Long> {
}
