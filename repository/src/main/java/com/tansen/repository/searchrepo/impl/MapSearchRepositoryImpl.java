package com.tansen.repository.searchrepo.impl;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.utility.SearchParamUtil;
import com.tansen.entity.Complaint;
import com.tansen.entity.ComplaintCoordinates;
import com.tansen.entity.enums.Priority;
import com.tansen.repository.searchrepo.MapSearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MapSearchRepositoryImpl implements MapSearchRepository {
    @PersistenceContext
    protected EntityManager em;
    @Override
    public Long count(SearchParam searchParam) {
        return count(searchParam, null);
    }


    @Override
    public List<ComplaintCoordinates> getAll(SearchParam searchParam) {
        return getAll(searchParam, null);
    }

    @Override
    public Long count(SearchParam searchParam, Long municipalityId) {
        return (Long) em.createQuery("""
        SELECT COUNT(cc.id)
        FROM ComplaintCoordinates cc
        JOIN cc.complaint c
        JOIN c.municipality mu
        WHERE
            mu.id = :municipalityId
            AND (:complaintTitle IS NULL OR LOWER(c.complaintTitle) LIKE LOWER(CONCAT('%', :complaintTitle, '%')))
        """)
                .setParameter("municipalityId", municipalityId)
                .setParameter("complaintTitle", SearchParamUtil.getString(searchParam, "complaintTitle"))
                .getSingleResult();
    }

    @Override
    public List<ComplaintCoordinates> getAll(SearchParam searchParam, Long municipalityId) {
        return em.createQuery("""
        SELECT cc
        FROM ComplaintCoordinates cc
        JOIN cc.complaint c
        JOIN c.municipality mu
        WHERE
            mu.id = :municipalityId
        """, ComplaintCoordinates.class)
                .setParameter("municipalityId", municipalityId)
                .setFirstResult(searchParam.getFirstRow())
                .setMaxResults(searchParam.getPageSize())
                .getResultList();
    }

}
