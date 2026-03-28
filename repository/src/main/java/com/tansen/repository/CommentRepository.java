package com.tansen.repository;

import com.tansen.entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("""
     SELECT c FROM Comment c
          WHERE c.complaint.uniqueId = :uniqueId
          AND c.complaint.municipality.id = :municipalityId
     """)
    List<Comment> findAllByComplaintUniqueIdAndMunicipality(
            @Param("uniqueId") String uniqueId,
            @Param("municipalityId") Long municipalityId
    );
    @Query("""
    SELECT  c FROM Comment c
        WHERE   c.complaint.uniqueId = :complaintUniqueId
        AND c.isDelete = false
    """)
    List<Comment> findCommentByComplaintUniqueId(@Param("complaintUniqueId") String complaintUniqueId);
    List<Comment> findByComplaintUniqueIdAndIsDeleteFalse(String complaintUniqueId);

    @Query("""
    SELECT c FROM Comment c
    WHERE c.uniqueId = :commentUniqueId
      AND c.complaint.uniqueId = :complaintUniqueId
      AND c.complaint.municipality.id = :municipalityId
      AND c.commentBy.uniqueId = :commentByUniqueId
""")
    Optional<Comment> findCommentByUniqueIds(
            @Param("commentUniqueId") String commentUniqueId,
            @Param("complaintUniqueId") String complaintUniqueId,
            @Param("commentByUniqueId") String commentByUniqueId,
            @Param("municipalityId") Long municipalityId
    );


}
