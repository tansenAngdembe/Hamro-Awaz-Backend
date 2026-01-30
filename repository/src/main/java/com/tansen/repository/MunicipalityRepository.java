package com.tansen.repository;

import com.tansen.entity.Municipality;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MunicipalityRepository extends JpaRepository<Municipality, Integer> {
    boolean existsByCode(String code);

    boolean existsByEmail(String email);

   Optional<Municipality> findByUniqueId(@NotBlank(message = "Unique ID is required") String uniqueId);
}
