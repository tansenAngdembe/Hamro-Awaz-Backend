package com.tansen.repository;

import com.tansen.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MunicipalityRepository extends JpaRepository<Municipality, Integer> {
    boolean existsByCode(String code);

    boolean existsByEmail(String email);
}
