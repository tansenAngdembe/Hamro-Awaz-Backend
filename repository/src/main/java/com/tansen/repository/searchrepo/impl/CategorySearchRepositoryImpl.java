package com.tansen.repository.searchrepo.impl;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.utility.SearchParamUtil;
import com.tansen.entity.Category;
import com.tansen.entity.Complaint;
import com.tansen.entity.enums.Priority;
import com.tansen.repository.searchrepo.CategorySearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategorySearchRepositoryImpl implements CategorySearchRepository {

    @PersistenceContext
    private EntityManager em;
    @Override
    public Long count(SearchParam searchParam) {
        return count(searchParam, null);
    }


    @Override
    public List<Category> getAll(SearchParam searchParam) {
        return getAll(searchParam, null);
    }


    @Override
    public Long count(SearchParam searchParam, Long municipalityId) {
        return (Long) em.createQuery("""
            SELECT COUNT(c.id)
            FROM Category c
            WHERE
                c.municipality.id = :municipalityId
                AND (:uniqueId IS NULL OR c.uniqueId LIKE CONCAT('%', :uniqueId, '%'))
                AND (:categoryName IS NULL OR c.categoryName LIKE CONCAT('%', :categoryName, '%'))
        """)
                .setParameter("municipalityId", municipalityId)
                .setParameter("uniqueId", SearchParamUtil.getString(searchParam, "uniqueId"))
                .setParameter("categoryName", SearchParamUtil.getString(searchParam, "categoryName"))
                .getSingleResult();
    }

    @Override
    public List<Category> getAll(SearchParam searchParam, Long municipalityId) {

        return em.createQuery("""
            SELECT c
            FROM Category c
            WHERE
                c.municipality.id = :municipalityId
                AND (:uniqueId IS NULL OR c.uniqueId LIKE CONCAT('%', :uniqueId, '%'))
                AND (:categoryName IS NULL OR c.categoryName LIKE CONCAT('%', :categoryName, '%'))
            ORDER BY c.createdAt DESC
        """, Category.class)
                .setParameter("municipalityId", municipalityId)
                .setParameter("uniqueId", SearchParamUtil.getString(searchParam, "uniqueId"))
                .setParameter("categoryName", SearchParamUtil.getString(searchParam, "categoryName"))
                .setFirstResult(searchParam.getFirstRow())
                .setMaxResults(searchParam.getPageSize())
                .getResultList();
    }


}

