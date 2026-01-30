package com.tansen.repository.searchrepo.impl;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.utility.SearchParamUtil;
import com.tansen.entity.AuthorityUser;
import com.tansen.repository.searchrepo.AuthorityUserSearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tansen.common.constant.SearchParamConstant.*;

@Repository
@RequiredArgsConstructor
public class AuthorityUserSearchRepositoryImpl implements AuthorityUserSearchRepository {
    @PersistenceContext
    protected EntityManager em;

    @Override
    public Long count(SearchParam searchParam) {
        return (Long) em.createQuery("""
                SELECT COUNT(u.id)
                FROM AuthorityUser  u
                JOIN u.municipality m
                JOIN u.authorityAccessGroup g
                WHERE
                    (:name IS NULL OR u.name LIKE CONCAT('%', :name, '%')) AND
                    (:email IS NULL OR u.email LIKE CONCAT('%', :email, '%')) AND
                    (:phoneNumber IS NULL OR u.phoneNumber LIKE CONCAT('%', :phoneNumber, '%')) AND
                    (:address IS NULL OR u.address LIKE CONCAT('%', :address, '%')) AND
                    (:municipality IS NULL OR m.governmentName LIKE CONCAT('%', :municipality, '%')) AND
                    (:municipalityUniqueId IS NULL OR m.uniqueId LIKE CONCAT('%', :municipalityUniqueId, '%'))

                """)
                .setParameter("name", SearchParamUtil.getString(searchParam, USERNAME))
                .setParameter("email", SearchParamUtil.getString(searchParam, EMAIL))
                .setParameter("phoneNumber", SearchParamUtil.getString(searchParam, PHONE_NUMBER))
                .setParameter("address", SearchParamUtil.getString(searchParam, ADDRESS))
                .setParameter("municipality", SearchParamUtil.getString(searchParam, MUNICIPALITY))
                .setParameter("municipalityUniqueId", SearchParamUtil.getString(searchParam, MUNICIPALITY_UNIQUE_ID))

                .getSingleResult();
    }

    @Override
    public List<AuthorityUser> getAll(SearchParam searchParam) {
        return em.createQuery("""
                SELECT u
                FROM AuthorityUser u
                JOIN u.municipality m
                JOIN u.authorityAccessGroup g
                WHERE
                    (:name IS NULL OR u.name LIKE CONCAT('%', :name, '%')) AND
                    (:email IS NULL OR u.email LIKE CONCAT('%', :email, '%')) AND
                    (:phoneNumber IS NULL OR u.phoneNumber LIKE CONCAT('%', :phoneNumber, '%')) AND
                    (:address IS NULL OR u.address LIKE CONCAT('%', :address, '%')) AND
                    (:municipality IS NULL OR m.governmentName LIKE CONCAT('%', :municipality, '%')) AND
                    (:municipalityUniqueId IS NULL OR m.uniqueId LIKE CONCAT('%', :municipalityUniqueId, '%'))

                """, AuthorityUser.class)
                .setParameter("name", SearchParamUtil.getString(searchParam, USERNAME))
                .setParameter("email", SearchParamUtil.getString(searchParam, EMAIL))
                .setParameter("phoneNumber", SearchParamUtil.getString(searchParam, MOBILE_NUMBER))
                .setParameter("address", SearchParamUtil.getString(searchParam, ADDRESS))
                .setParameter("municipality", SearchParamUtil.getString(searchParam, MUNICIPALITY))
                .setParameter("municipalityUniqueId", SearchParamUtil.getString(searchParam, MUNICIPALITY_UNIQUE_ID))
                .setFirstResult(searchParam.getFirstRow())
                .setMaxResults(searchParam.getPageSize())
                .getResultList();
    }
}
