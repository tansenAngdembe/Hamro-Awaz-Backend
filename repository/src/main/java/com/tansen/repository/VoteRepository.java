package com.tansen.repository;

import com.tansen.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    boolean existsByComplaintIdAndUserId(Long complaintId, Long userId);

    long countByComplaintId(Long complaintId);

    @Query("SELECT v.complaint.id, COUNT(v) FROM Vote v WHERE v.complaint.id IN :ids GROUP BY v.complaint.id")
    List<Object[]> countVotesByComplaintIds(@Param("ids") List<Long> complaintIds);
}
