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
            SELECT COUNT(m.id)
            FROM ComplaintCoordinates m
            JOIN m.complaint c
            WHERE
                c.id = :municipality AND
                (:latitude IS NULL OR m.latitude LIKE CONCAT('%', :latitude, '%')) AND
                (:complaintTitle IS NULL OR c.complaintTitle LIKE CONCAT('%', :complaintTitle, '%'))
            """)
                .setParameter("municipality", municipalityId)
                .setParameter("latitude", SearchParamUtil.getBigDecimal(searchParam, "latitude"))
                .setParameter("complaintTitle", SearchParamUtil.getString(searchParam, "complaintTitle"))
                .getSingleResult();
    }


    @Override
    public List<ComplaintCoordinates> getAll(SearchParam searchParam, Long municipalityId) {
        return em.createQuery("""
            SELECT c
            FROM ComplaintCoordinates c
            JOIN c.municipality m
            WHERE
                m.id = :municipalityId AND
                (:latitude IS NULL OR c.latitude LIKE CONCAT('%', :latitude, '%')) AND
            """, ComplaintCoordinates.class)
                .setParameter("municipalityId", municipalityId)
                .setParameter("uniqueId", SearchParamUtil.getString(searchParam, "uniqueId"))
                .setFirstResult(searchParam.getFirstRow())
                .setMaxResults(searchParam.getPageSize())
                .getResultList();
    }
}
