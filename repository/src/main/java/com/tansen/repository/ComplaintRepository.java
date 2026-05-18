package com.tansen.repository;

import com.tansen.common.dto.ComplaintStats;
import com.tansen.entity.Complaint;
import com.tansen.entity.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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

    // ComplaintRepository
    @Query("SELECT c FROM Complaint c " +
            "JOIN FETCH c.category " +
            "JOIN FETCH c.municipality " +
            "LEFT JOIN FETCH c.assignedTo " +
            "WHERE c.priority <> :priority")
    List<Complaint> findByPriorityNot(@Param("priority") Priority priority);


    @Query("""
        SELECT
            COUNT(c) AS total,

            SUM(CASE WHEN c.status.name = 'RESOLVED' THEN 1 ELSE 0 END) AS resolved,
            SUM(CASE WHEN c.status.name = 'PENDING' THEN 1 ELSE 0 END) AS pending,
            SUM(CASE WHEN c.status.name = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS inProgress,
            SUM(CASE WHEN c.escalation IS NOT NULL THEN 1 ELSE 0 END) AS escalated,

            COUNT(comments) AS totalComments,

            0 AS totalVotes,
            0 AS upVotes,
            0 AS downVotes,

            SUM(CASE WHEN c.resolvedAt IS NULL AND c.createdDate < :threshold THEN 1 ELSE 0 END) AS slaBreached,

            (SUM(CASE WHEN c.escalation IS NOT NULL THEN 1 ELSE 0 END) / COUNT(c)) * 100 AS escalationRate

        FROM Complaint c
        LEFT JOIN c.comments comments
        WHERE c.createdDate BETWEEN :from AND :to
        AND c.municipality = :administrativeId
    """)
    ComplaintStats fetchStats(LocalDateTime from, LocalDateTime to,Long administrativeId);

}
