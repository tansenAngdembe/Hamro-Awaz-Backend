package com.tansen.repository;

import com.tansen.entity.AccessGroup;
import com.tansen.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccessGroupRepository extends JpaRepository<AccessGroup, Long> {
    Optional<AccessGroup> findByName(String name);

    List<AccessGroup> findByStatus(Status status);
}
