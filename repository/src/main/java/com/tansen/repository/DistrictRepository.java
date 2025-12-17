package com.tansen.repository;

import com.tansen.entity.District;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository  extends JpaRepository<District,Long> {
    List<District> findDistrictsByProvinceId(@NotNull(message = "Province id is required") Long provinceId);
}
