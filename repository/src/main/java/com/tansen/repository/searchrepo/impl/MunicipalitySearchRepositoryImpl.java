package com.tansen.repository.searchrepo.impl;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.utility.SearchParamUtil;
import com.tansen.entity.Municipality;
import com.tansen.repository.searchrepo.MunicipalitySearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MunicipalitySearchRepositoryImpl implements MunicipalitySearchRepository {
    @PersistenceContext
    protected EntityManager em;
    @Override
    public Long count(SearchParam searchParam) {
        return (Long) em.createQuery("""
                SELECT COUNT(m.id)
                FROM Municipality m
                JOIN m.province p
                JOIN m.district d
                JOIN m.localLevel l
                WHERE
                    (:governmentName IS NULL OR m.governmentName LIKE CONCAT('%', :governmentName, '%')) AND
                    (:code IS NULL OR m.code LIKE CONCAT('%', :code, '%')) AND
                    (:province IS NULL OR p.province = :province) AND
                    (:district IS NULL OR d.districtName = :district)
                """)
                .setParameter("governmentName", SearchParamUtil.getString(searchParam, "governmentName"))
                .setParameter("code", SearchParamUtil.getString(searchParam, "code"))
                .setParameter("province", SearchParamUtil.getString(searchParam, "province"))
                .setParameter("district", SearchParamUtil.getString(searchParam, "district"))
                .getSingleResult();
    }

    @Override
    public List<Municipality> getAll(SearchParam searchParam) {
        return em.createQuery("""
                SELECT m
                FROM Municipality m 
                JOIN m.province p 
                JOIN m.district d 
                JOIN m.localLevel l 
                WHERE
                    (:governmentName IS NULL OR m.governmentName LIKE CONCAT('%', :governmentName, '%')) AND
                    (:code IS NULL OR m.code LIKE CONCAT('%', :code, '%')) AND
                    (:province IS NULL OR p.province = :province) AND
                    (:district IS NULL OR d.districtName = :district) 
                """, Municipality.class)
                .setParameter("governmentName", SearchParamUtil.getString(searchParam, "governmentName"))
                .setParameter("code", SearchParamUtil.getString(searchParam, "code"))
                .setParameter("province", SearchParamUtil.getString(searchParam, "province"))
                .setParameter("district", SearchParamUtil.getString(searchParam, "district"))
                .setFirstResult(searchParam.getFirstRow())
                .setMaxResults(searchParam.getPageSize())
                .getResultList();
    }
}

