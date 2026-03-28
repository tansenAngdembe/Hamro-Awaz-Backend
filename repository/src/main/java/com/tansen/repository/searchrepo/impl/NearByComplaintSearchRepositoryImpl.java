package com.tansen.repository.searchrepo.impl;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.utility.SearchParamUtil;
import com.tansen.entity.Complaint;
import com.tansen.repository.searchrepo.NearByComplaintSearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class NearByComplaintSearchRepositoryImpl implements NearByComplaintSearchRepository {

        @PersistenceContext
        private EntityManager em;

        @Override
        public Long countNearby( SearchParam searchParam,
                BigDecimal latitude,
                BigDecimal longitude,
                Double radiusKm
        ) {

            return em.createQuery("""
            SELECT COUNT(c.id)
            FROM Complaint c
            JOIN ComplaintCoordinates cc ON cc.complaint = c
            WHERE
                (
                    6371 * acos(
                        cos(radians(:lat)) * cos(radians(cc.latitude)) *
                        cos(radians(cc.longitude) - radians(:lng)) +
                        sin(radians(:lat)) * sin(radians(cc.latitude))
                    )
                ) <= :radius
                AND (:statusId IS NULL OR c.status.id = :statusId)
                AND (:categoryId IS NULL OR c.category.id = :categoryId)
                AND c.active = true
        """, Long.class)
                    .setParameter("lat", latitude.doubleValue())
                    .setParameter("lng", longitude.doubleValue())
                    .setParameter("radius", radiusKm)
                    .setParameter("statusId", SearchParamUtil.getLong(searchParam, "statusId"))
                    .setParameter("categoryId", SearchParamUtil.getLong(searchParam, "categoryId"))
                    .getSingleResult();
        }

        @Override
        public List<Complaint> findNearby(
                                SearchParam searchParam,
                                BigDecimal latitude,
                BigDecimal longitude,
                Double radiusKm
        ) {

            return em.createQuery("""
            SELECT c
            FROM Complaint c
            JOIN ComplaintCoordinates cc ON cc.complaint = c
            WHERE
                (
                    6371 * acos(
                        cos(radians(:lat)) * cos(radians(cc.latitude)) *
                        cos(radians(cc.longitude) - radians(:lng)) +
                        sin(radians(:lat)) * sin(radians(cc.latitude))
                    )
                ) <= :radius
                AND (:statusId IS NULL OR c.status.id = :statusId)
                AND (:categoryId IS NULL OR c.category.id = :categoryId)
                AND c.active = true
            ORDER BY c.createdDate DESC
        """, Complaint.class)
                    .setParameter("lat", latitude.doubleValue())
                    .setParameter("lng", longitude.doubleValue())
                    .setParameter("radius", radiusKm)
                    .setParameter("statusId", SearchParamUtil.getLong(searchParam, "statusId"))
                    .setParameter("categoryId", SearchParamUtil.getLong(searchParam, "categoryId"))
                    .setFirstResult(searchParam.getFirstRow())
                    .setMaxResults(searchParam.getPageSize())
                    .getResultList();
        }
}


