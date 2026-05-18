package com.tansen.repository;

import com.tansen.entity.AdministrativeUnit;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministrativeUnitRepository extends JpaRepository<AdministrativeUnit, Integer> {
    boolean existsByCode(String code);

    boolean existsByEmail(String email);
    Optional<AdministrativeUnit> findByProvince_Id(Long provinceId);

    Optional<AdministrativeUnit> findByUniqueId(@NotBlank(message = "Unique ID is required") String uniqueId);
}
