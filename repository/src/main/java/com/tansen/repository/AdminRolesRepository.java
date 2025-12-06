package com.tansen.repository;

import com.tansen.entity.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRolesRepository extends JpaRepository<AdminRole, Long> {
    AdminRole findByName(String roleName);
}
