package com.tansen.repository;

import com.tansen.entity.Category;
import com.tansen.entity.Escalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EscalationRepository extends JpaRepository<Escalation, Long> {
    Optional<Escalation> findByCategoryAndActiveTrue(Category category);
}
