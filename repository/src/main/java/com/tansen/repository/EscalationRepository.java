package com.tansen.repository;

import com.tansen.entity.Category;
import com.tansen.entity.Escalation;
import com.tansen.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EscalationRepository extends JpaRepository<Escalation, Long> {
    Optional<Escalation> findByCategoryAndActiveTrue(Category category);

    // Exact match: category + municipality
    Optional<Escalation> findByMunicipalityAndCategoryAndActiveTrue(
            Municipality municipality, Category category);

    // Fallback: municipality-wide rule (no specific category)
    Optional<Escalation> findByMunicipalityAndCategoryIsNullAndActiveTrue(
            Municipality municipality);
}
