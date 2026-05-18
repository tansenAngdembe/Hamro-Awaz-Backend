package com.tansen.repository.searchrepo.impl;

import com.tansen.common.dto.SearchFieldParam;
import com.tansen.common.dto.SearchParam;
import com.tansen.common.utility.SearchParamUtil;
import com.tansen.entity.Complaint;
import com.tansen.entity.enums.Priority;
import com.tansen.repository.searchrepo.MyComplaintSearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tansen.common.constant.SearchParamConstant.*;

@Repository
@RequiredArgsConstructor
public class MyComplaintSearchRepositoryImpl implements MyComplaintSearchRepository {
        @PersistenceContext
        private EntityManager em;

    @Override
    public Long count(SearchParam searchParam, Long userId) {
        return (Long) em.createQuery("SELECT COUNT(c.id) " +
                        "FROM Complaint c " +
                        "JOIN c.status s " +
                        "JOIN c.category cat " +
                        "JOIN c.reportedBy u " +
                        "WHERE u.id = :userId " +
                        "AND c.active = true " +
                        "AND (:title IS NULL OR c.complaintTitle LIKE CONCAT('%', :title, '%')) " +
                        "AND (:description IS NULL OR c.complaintDescription LIKE CONCAT('%', :description, '%')) " +
                        "AND (:uniqueId IS NULL OR c.uniqueId LIKE CONCAT('%', :uniqueId, '%')) " +
                        "AND (:status IS NULL OR s.description = :status) " +
                        "AND (:category IS NULL OR cat.categoryName = :category) " +
                        "AND (:priority IS NULL OR c.priority = :priority) ")
                .setParameter("userId", userId)
                .setParameter("title", SearchParamUtil.getString(searchParam, COMPLAINT_TITLE))
                .setParameter("description", SearchParamUtil.getString(searchParam, COMPLAINT_DESCRIPTION))
                .setParameter("uniqueId", SearchParamUtil.getString(searchParam, UNIQUE_ID))
                .setParameter("status", SearchParamUtil.getString(searchParam, STATUS))
                .setParameter("category", SearchParamUtil.getString(searchParam, CATEGORY))
                .setParameter("priority", SearchParamUtil.getString(searchParam, PRIORITY))
                .getSingleResult();
    }

    @Override
    public List<Complaint> getAll(SearchParam searchParam, Long userId) {
        return em.createQuery("SELECT c " +
                        "FROM Complaint c " +
                        "JOIN c.status s " +
                        "JOIN c.category cat " +
                        "JOIN c.reportedBy u " +
                        "WHERE u.id = :userId " +
                        "AND c.active = true " +
                        "AND (:title IS NULL OR c.complaintTitle LIKE CONCAT('%', :title, '%')) " +
                        "AND (:description IS NULL OR c.complaintDescription LIKE CONCAT('%', :description, '%')) " +
                        "AND (:uniqueId IS NULL OR c.uniqueId LIKE CONCAT('%', :uniqueId, '%')) " +
                        "AND (:status IS NULL OR s.description = :status) " +
                        "AND (:category IS NULL OR cat.categoryName = :category) " +
                        "AND (:priority IS NULL OR c.priority = :priority) " +
                        "ORDER BY c.createdDate DESC", Complaint.class)
                .setParameter("userId", userId)
                .setParameter("title", SearchParamUtil.getString(searchParam, COMPLAINT_TITLE))
                .setParameter("description", SearchParamUtil.getString(searchParam, COMPLAINT_DESCRIPTION))
                .setParameter("uniqueId", SearchParamUtil.getString(searchParam, UNIQUE_ID))
                .setParameter("status", SearchParamUtil.getString(searchParam, STATUS))
                .setParameter("category", SearchParamUtil.getString(searchParam, CATEGORY))
                .setParameter("priority", SearchParamUtil.getString(searchParam, PRIORITY))
                .setFirstResult(searchParam.getFirstRow())
                .setMaxResults(searchParam.getPageSize())
                .getResultList();
    }

}
