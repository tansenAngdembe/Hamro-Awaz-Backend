package com.tansen.repository;

import com.tansen.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistrictRepository  extends JpaRepository<District,Long> {
}
