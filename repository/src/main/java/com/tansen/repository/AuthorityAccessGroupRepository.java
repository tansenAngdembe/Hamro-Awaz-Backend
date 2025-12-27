package com.tansen.repository;

import com.tansen.entity.AuthorityAccessGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorityAccessGroupRepository extends JpaRepository<AuthorityAccessGroup,Long> {
Optional<AuthorityAccessGroup> findByName(String name);
}
