package com.tansen.repository;

import com.tansen.entity.AuthorityAccessGroupRoleMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityAccessGroupRoleMapRepository extends JpaRepository<AuthorityAccessGroupRoleMap,Long> {
}
