package com.tansen.repository.searchrepo.impl;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.utility.SearchParamUtil;
import com.tansen.entity.Escalation;
import com.tansen.repository.searchrepo.EscalationSearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EscalationSearchRepositoryImpl implements EscalationSearchRepository {
    @PersistenceContext
    private EntityManager em;
    @Override
    public Long count(SearchParam searchParam) {
        return count(searchParam, null);
    }


    @Override
    public List<Escalation> getAll(SearchParam searchParam) {
        return getAll(searchParam, null);
    }


    @Override
    public Long count(SearchParam searchParam, Long municipalityId) {
        return (Long) em.createQuery("""
            SELECT COUNT(c.id)
            FROM Escalation e
            JOIN e.category c
            WHERE
                e.municipality.id = :municipalityId
                AND (:ruleName IS NULL OR e.ruleName LIKE CONCAT('%', :ruleName, '%'))
                AND (:categoryName IS NULL OR c.categoryName LIKE CONCAT('%', :categoryName, '%'))
        """)
                .setParameter("municipalityId", municipalityId)
                .setParameter("ruleName", SearchParamUtil.getString(searchParam, "ruleName"))
                .setParameter("categoryName", SearchParamUtil.getString(searchParam, "categoryName"))
                .getSingleResult();
    }

    @Override
    public List<Escalation> getAll(SearchParam searchParam, Long municipalityId) {

        return em.createQuery("""
            SELECT e
            FROM Escalation e
            JOIN e.category c
            WHERE
                e.municipality.id = :municipalityId
                AND (:ruleName IS NULL OR e.ruleName LIKE CONCAT('%', :ruleName, '%'))
                AND (:categoryName IS NULL OR c.categoryName LIKE CONCAT('%', :categoryName, '%'))
            ORDER BY c.createdAt DESC
        """, Escalation.class)
                .setParameter("municipalityId", municipalityId)
                .setParameter("ruleName", SearchParamUtil.getString(searchParam, "ruleName"))
                .setParameter("categoryName", SearchParamUtil.getString(searchParam, "categoryName"))
                .setFirstResult(searchParam.getFirstRow())
                .setMaxResults(searchParam.getPageSize())
                .getResultList();
    }
}
