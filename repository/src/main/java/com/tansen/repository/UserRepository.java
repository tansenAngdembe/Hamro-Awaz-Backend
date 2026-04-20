package com.tansen.repository;

import com.tansen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Modifying
    @Query("UPDATE User u SET u.lastLoggedInTime = :time WHERE u.email = :email")

    void updateLastLoggedInTime(@Param("email") String email, @Param("time") LocalDateTime time);

    @Modifying
    @Query("UPDATE User u SET u.wrongPasswordAttemptCount = :count WHERE u.email = :email")
    void updateWrongPasswordAttemptCount(@Param("email") String email, @Param("count") Integer count);

    Optional<User> findByPhoneNumber(String mobileNumber);

    User findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    User findByEmailOrPhoneNumber(String email, String phoneNumber);

    Optional<User> findByUniqueId(String uniqueId);
}
