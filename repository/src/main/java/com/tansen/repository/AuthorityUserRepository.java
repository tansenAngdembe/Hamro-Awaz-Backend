package com.tansen.repository;

import com.tansen.entity.AuthorityUser;
import com.tansen.entity.Municipality;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorityUserRepository extends JpaRepository<AuthorityUser,Long> {
    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Email must be valid") String authorityAdminEmail);

    List<AuthorityUser> findByMunicipality(Municipality municipality);

    boolean existsByPhoneNumber(@NotBlank(message = "Mobile number is required") String mobileNumber);

    Optional<AuthorityUser> findByUniqueId(@NotBlank(message = "Unique ID is required") String uniqueId);

    Optional<AuthorityUser> findByEmail(String email);

    Optional<AuthorityUser> findByPhoneNumber(String phoneNumber);
}
