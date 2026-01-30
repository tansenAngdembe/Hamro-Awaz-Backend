package com.tansen.repository.searchrepo.impl;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.utility.SearchParamUtil;
import com.tansen.entity.Complaint;
import com.tansen.entity.enums.Priority;
import com.tansen.repository.searchrepo.ComplaintSearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ComplaintSearchRepositoryImpl implements ComplaintSearchRepository {

    @PersistenceContext
    protected EntityManager em;
    @Override
    public Long count(SearchParam searchParam) {
        return count(searchParam, null);
    }


    @Override
    public List<Complaint> getAll(SearchParam searchParam) {
        return getAll(searchParam, null);
    }

    @Override
    public Long count(SearchParam searchParam, Long municipalityId) {
        return (Long) em.createQuery("""
            SELECT COUNT(c.id)
            FROM Complaint c
            JOIN c.category cat
            JOIN c.status st
            JOIN c.reportedBy rb
            JOIN c.municipality m
            LEFT JOIN c.assignedTo at
            WHERE
                m.id = :municipalityId AND
                (:uniqueId IS NULL OR c.uniqueId LIKE CONCAT('%', :uniqueId, '%')) AND
                (:complaintTitle IS NULL OR c.complaintTitle LIKE CONCAT('%', :complaintTitle, '%')) AND
                (:category IS NULL OR cat.categoryName = :category) AND
                (:status IS NULL OR st.description = :status) AND
                (:priority IS NULL OR c.priority = :priority) AND
                (:active IS NULL OR c.active = :active)
            """)
                .setParameter("municipalityId", municipalityId)
                .setParameter("uniqueId", SearchParamUtil.getString(searchParam, "uniqueId"))
                .setParameter("complaintTitle", SearchParamUtil.getString(searchParam, "complaintTitle"))
                .setParameter("category", SearchParamUtil.getString(searchParam, "category"))
                .setParameter("status", SearchParamUtil.getString(searchParam, "status"))
                .setParameter("priority", SearchParamUtil.getEnum(searchParam, "priority", Priority.class))
                .setParameter("active", SearchParamUtil.getBoolean(searchParam, "active"))
                .getSingleResult();
    }


    @Override
    public List<Complaint> getAll(SearchParam searchParam, Long municipalityId) {
        return em.createQuery("""
            SELECT c
            FROM Complaint c
            JOIN c.category cat
            JOIN c.status st
            JOIN c.reportedBy rb
            JOIN c.municipality m
            LEFT JOIN c.assignedTo at
            WHERE
                m.id = :municipalityId AND
                (:uniqueId IS NULL OR c.uniqueId LIKE CONCAT('%', :uniqueId, '%')) AND
                (:complaintTitle IS NULL OR c.complaintTitle LIKE CONCAT('%', :complaintTitle, '%')) AND
                (:category IS NULL OR cat.categoryName = :category) AND
                (:status IS NULL OR st.description = :status) AND
                (:priority IS NULL OR c.priority = :priority) AND
                (:active IS NULL OR c.active = :active)
            ORDER BY c.createdDate DESC
            """, Complaint.class)
                .setParameter("municipalityId", municipalityId)
                .setParameter("uniqueId", SearchParamUtil.getString(searchParam, "uniqueId"))
                .setParameter("complaintTitle", SearchParamUtil.getString(searchParam, "complaintTitle"))
                .setParameter("category", SearchParamUtil.getString(searchParam, "category"))
                .setParameter("status", SearchParamUtil.getString(searchParam, "status"))
                .setParameter("priority", SearchParamUtil.getEnum(searchParam, "priority", Priority.class))
                .setParameter("active", SearchParamUtil.getBoolean(searchParam, "active"))
                .setFirstResult(searchParam.getFirstRow())
                .setMaxResults(searchParam.getPageSize())
                .getResultList();
    }



}
