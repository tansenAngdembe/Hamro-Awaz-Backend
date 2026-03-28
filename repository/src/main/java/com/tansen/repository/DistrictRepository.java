package com.tansen.repository;

import com.tansen.entity.District;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistrictRepository  extends JpaRepository<District,Long> {
    List<District> findDistrictsByProvinceId(@NotNull(message = "Province id is required") Long provinceId);
    Optional<District> findByUniqueId(@NotBlank(message = "Unique ID is required") String uniqueId);

}
