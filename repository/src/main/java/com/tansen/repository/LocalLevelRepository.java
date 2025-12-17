package com.tansen.repository;

import com.tansen.entity.LocalLevel;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalLevelRepository extends JpaRepository<LocalLevel, Long> {
    List<LocalLevel> findLocalLevelsByDistrictId(@NotNull(message = "District id is required") Long districtId);
}
