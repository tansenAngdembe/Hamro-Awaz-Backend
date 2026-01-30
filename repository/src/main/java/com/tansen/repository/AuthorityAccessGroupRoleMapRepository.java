package com.tansen.repository;

import com.tansen.entity.AuthorityAccessGroup;
import com.tansen.entity.AuthorityAccessGroupRoleMap;
import com.tansen.entity.AuthorityUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityAccessGroupRoleMapRepository extends JpaRepository<AuthorityAccessGroupRoleMap,Long> {
    @Query("SELECT rgm.authorityUserRole FROM AuthorityAccessGroupRoleMap rgm WHERE rgm.authorityAccessGroup.id = :groupId AND rgm.isActive = true")
    List<AuthorityUserRole> getRolesByAccessGroup(@Param("groupId") Long groupId);


List<AuthorityAccessGroupRoleMap> findByAuthorityAccessGroup(AuthorityAccessGroup accessGroup);
}
