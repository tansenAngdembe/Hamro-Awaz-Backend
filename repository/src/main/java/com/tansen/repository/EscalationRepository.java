package com.tansen.repository;

import com.tansen.entity.AdministrativeUnit;
import com.tansen.entity.Category;
import com.tansen.entity.Escalation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EscalationRepository extends JpaRepository<Escalation, Long> {
    Optional<Escalation> findByCategoryAndActiveTrue(Category category);

    // Exact match: category + municipality
    Optional<Escalation> findByMunicipalityAndCategoryAndActiveTrue(
            AdministrativeUnit municipality, Category category);

    // Fallback: municipality-wide rule (no specific category)
    Optional<Escalation> findByMunicipalityAndCategoryIsNullAndActiveTrue(
            AdministrativeUnit municipality);

    // EscalationRepository
    Optional<Escalation> findByCategoryAndMunicipalityAndActiveTrue(
            Category category,
            AdministrativeUnit municipality
    );

    // And for the optimized bulk fetch
    List<Escalation> findAllByActiveTrue();
}
