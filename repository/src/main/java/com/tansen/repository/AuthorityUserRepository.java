package com.tansen.repository;

import com.tansen.entity.AuthorityUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityUserRepository extends JpaRepository<AuthorityUser,Long> {
    boolean existsByEmail(@NotBlank(message = "Email is required") @Email(message = "Email must be valid") String authorityAdminEmail);
}
