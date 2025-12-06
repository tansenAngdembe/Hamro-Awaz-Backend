package com.tansen.repository;

import com.tansen.entity.Admin;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByEmail(@NotBlank(message="Email||Phone is required") String email);

    @Modifying
    @Query("UPDATE Admin u SET u.lastLoggedInTime = :time WHERE u.email = :email")
    void updateLastLoggedInTime(@Param("email") String email, @Param("time") LocalDateTime time);

    @Modifying
    @Query("UPDATE Admin u SET u.wrongPasswordAttemptCount = :count WHERE u.email = :email")
    void updateWrongPasswordAttemptCount(@Param("email") String email, @Param("count") Integer count);


    Optional<Admin> findByMobileNumber(String mobileNumber);

    Optional<Admin> findByUniqueId(@NotBlank(message = "Unique id is required") String uniqueId);
}
