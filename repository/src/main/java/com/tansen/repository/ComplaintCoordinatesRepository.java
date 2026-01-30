package com.tansen.repository;

import com.tansen.entity.ComplaintCoordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintCoordinatesRepository extends JpaRepository<ComplaintCoordinates,Long> {

}
