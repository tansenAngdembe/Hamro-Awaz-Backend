package com.tansen.repository;

import com.tansen.entity.AuthorityUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityUserRoleRepository extends JpaRepository<AuthorityUserRole,Long> {
}
