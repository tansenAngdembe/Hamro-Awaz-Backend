package com.tansen.repository;

import com.tansen.entity.User;
import com.tansen.entity.UserRegistrationEmailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRegistrationEmailLogRepository extends JpaRepository<UserRegistrationEmailLog, Long> {
    List<UserRegistrationEmailLog> findByUserAndIsOtpExpiredFalse(User user);

    UserRegistrationEmailLog findByUserAndOtpAndIsOtpExpiredFalse(User user, String otp);
}

