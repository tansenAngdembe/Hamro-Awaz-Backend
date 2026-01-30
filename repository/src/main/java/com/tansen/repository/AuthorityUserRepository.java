package com.tansen.repository;

import com.tansen.entity.AuthorityUser;
import com.tansen.entity.Municipality;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorityUserRepository extends JpaRepository<AuthorityUser,Long> {
    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Email must be valid") String authorityAdminEmail);

    List<AuthorityUser> findByMunicipality(Municipality municipality);

    boolean existsByPhoneNumber(@NotBlank(message = "Mobile number is required") String mobileNumber);

    Optional<AuthorityUser> findByUniqueId(@NotBlank(message = "Unique ID is required") String uniqueId);


    Optional<AuthorityUser> findByEmail(String email);



    @Modifying
    @Transactional
    @Query("""
        UPDATE AuthorityUser u
        SET u.lastLoggedInTime = :time
        WHERE u.name = :name
    """)
    void updateLastLoggedInTime(
            @Param("name") String name,
            @Param("time") LocalDateTime time
    );
    @Modifying
    @Transactional
    @Query("""
        UPDATE AuthorityUser u
        SET u.wrongOtpAuthAttemptCount = :count
        WHERE u.name = :name
    """)
    void updateWrongOtpAuthAttemptCount(
            @Param("name") String name,
            @Param("count") Integer count
    );

    Optional<AuthorityUser> findByPhoneNumber(@NotBlank(message = "Mobile number is required") @Size(min = 10, max = 10, message = "Mobile number must be 10 digits") @Pattern(regexp="^(97|98)[0-9]{8}$", message = "Invalid mobile number format") String mobileNumber);
}
