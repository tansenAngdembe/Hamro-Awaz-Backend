package com.tansen.repository.searchrepo.impl;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.utility.SearchParamUtil;
import com.tansen.entity.AuthorityUserActionLog;
import com.tansen.repository.searchrepo.AuthorityUserActionLogSearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tansen.common.constant.SearchParamConstant.*;

@Repository
@RequiredArgsConstructor
public class AuthorityUserActionLogSearchRepositoryImpl implements AuthorityUserActionLogSearchRepository {
    @PersistenceContext
    private final EntityManager em;

    @Override
    public Long count(SearchParam searchParam) {
        return count(searchParam, null);
    }

    @Override
    public List<AuthorityUserActionLog> getAll(SearchParam searchParam) {
        return getAll(searchParam, null);
    }

    @Override
    public Long count(SearchParam searchParam, String vendorId) {
        return (Long) em.createQuery(
                        "SELECT COUNT(DISTINCT a.id) " +
                                "FROM AuthorityUserActionLog a " +
                                "JOIN a.actionBy vendor " +
                                "WHERE (:userEmail IS NULL OR vendor.email = :userEmail) " +
                                "AND (:remarks IS NULL OR a.remarks = :remarks) " +
                                "AND (:targetType IS NULL OR a.targetType = :targetType) " +
                                "AND (:actionType IS NULL OR a.actionType = :actionType) " +
                                "AND (:ipAddress IS NULL OR a.ipAddress = :ipAddress) " +
                                "AND (:vendorId IS NULL OR vendor.id = :vendorId)"
                )
                .setParameter("userEmail", SearchParamUtil.getString(searchParam, USER_EMAIL))
                .setParameter("remarks", SearchParamUtil.getString(searchParam, REMARKS))
                .setParameter("targetType", SearchParamUtil.getString(searchParam, TARGET_TYPE))
                .setParameter("actionType", SearchParamUtil.getString(searchParam, ACTION_TYPE))
                .setParameter("ipAddress", SearchParamUtil.getString(searchParam, IP_ADDRESS))
                .setParameter("vendorId", vendorId == null ? null : Integer.valueOf(vendorId))
                .getSingleResult();
    }

    @Override
    public List<AuthorityUserActionLog> getAll(SearchParam searchParam, String vendorId) {
        return em.createQuery(
                        "SELECT a " +
                                "FROM AuthorityUserActionLog a " +
                                "JOIN a.actionBy vendor " +
                                "WHERE (:userEmail IS NULL OR vendor.email = :userEmail) " +
                                "AND (:remarks IS NULL OR a.remarks = :remarks) " +
                                "AND (:targetType IS NULL OR a.targetType = :targetType) " +
                                "AND (:actionType IS NULL OR a.actionType = :actionType) " +
                                "AND (:ipAddress IS NULL OR a.ipAddress = :ipAddress) " +
                                "AND (:vendorId IS NULL OR vendor.id = :vendorId) ",
                        AuthorityUserActionLog.class
                )
                .setParameter("userEmail", SearchParamUtil.getString(searchParam, USER_EMAIL))
                .setParameter("remarks", SearchParamUtil.getString(searchParam, REMARKS))
                .setParameter("targetType", SearchParamUtil.getString(searchParam, TARGET_TYPE))
                .setParameter("actionType", SearchParamUtil.getString(searchParam, ACTION_TYPE))
                .setParameter("ipAddress", SearchParamUtil.getString(searchParam, IP_ADDRESS))
                .setParameter("vendorId", vendorId == null ? null : Integer.valueOf(vendorId))
                .setFirstResult(searchParam.getFirstRow())
                .setMaxResults(searchParam.getPageSize())
                .getResultList();
    }
}
