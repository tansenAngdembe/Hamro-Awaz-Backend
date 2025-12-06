package com.tansen.repository;

import com.tansen.entity.Admin;
import com.tansen.entity.AdminEmailLog;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminEmailLogRepository extends JpaRepository<AdminEmailLog, Long> {
    Optional<AdminEmailLog> findByUuid(@NotBlank(message = "Unique token is missing") String uuid);

    List<AdminEmailLog> findAllByAdminAndIsExpiredFalse(Admin admin);
}
