package com.tansen.repository;

import com.tansen.entity.LocalLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocalLevelRepository extends JpaRepository<LocalLevel, Long> {
}
