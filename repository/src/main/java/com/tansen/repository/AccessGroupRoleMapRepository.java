package com.tansen.repository;

import com.tansen.entity.AccessGroup;
import com.tansen.entity.AccessGroupRoleMap;
import com.tansen.entity.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessGroupRoleMapRepository extends JpaRepository<AccessGroupRoleMap, Long> {
    @Query("SELECT rgm.adminRole FROM AccessGroupRoleMap rgm WHERE rgm.accessGroup.id = :groupId AND rgm.isActive = true")
    List<AdminRole> getRolesByAccessGroup(@Param("groupId") Long groupId);

    List<AccessGroupRoleMap> findByAccessGroup(AccessGroup accessGroup);
}
