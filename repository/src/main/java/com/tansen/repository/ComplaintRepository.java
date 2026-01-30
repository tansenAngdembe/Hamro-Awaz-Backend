package com.tansen.repository;

import com.tansen.entity.Complaint;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint,Long> {

    Complaint findByUniqueId(@NotBlank(message = "Complaint uniqueId") String complaintUniqueId);

    @Query("""
    SELECT c
    FROM Complaint c
    JOIN c.municipality m
    WHERE c.uniqueId = :complaintUniqueId
      AND m.uniqueId = :municipalityUniqueId
""")
    Optional<Complaint> findByIdAndMunicipalityId(
            @Param("complaintUniqueId") String complaintUniqueId,
            @Param("municipalityUniqueId") String municipalityUniqueId
    );
}
