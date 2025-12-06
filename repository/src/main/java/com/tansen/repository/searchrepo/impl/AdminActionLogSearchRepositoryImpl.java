package com.tansen.repository.searchrepo.impl;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.utility.SearchParamUtil;
import com.tansen.entity.ActionLog;
import com.tansen.repository.searchrepo.AdminActionLogSearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

import static com.tansen.common.constant.SearchParamConstant.*;

@Repository
@RequiredArgsConstructor
public class AdminActionLogSearchRepositoryImpl implements AdminActionLogSearchRepository {
    @PersistenceContext
    protected EntityManager em;

    @Override
    public Long count(SearchParam searchParam) {
        return em.createQuery(
                        "select count(a.id) " +
                                "from ActionLog a " +
                                "join a.actionBy admin " +
                                "where " +
                                "(:remarks is null or a.remarks like concat('%', :remarks, '%')) and " +
                                "(:targetType is null or a.targetType = :targetType) and " +
                                "(:actionType is null or a.actionType = :actionType) and " +
                                "(:ipAddress is null or a.ipAddress like concat('%', :ipAddress, '%')) and " +
                                "(:actionBy is null or admin.email like concat('%', :actionBy, '%'))"
                        , Long.class)
                .setParameter("remarks", SearchParamUtil.getString(searchParam, REMARKS))
                .setParameter("targetType", SearchParamUtil.getString(searchParam, TARGET_TYPE))
                .setParameter("actionType", SearchParamUtil.getString(searchParam, ACTION_TYPE))
                .setParameter("ipAddress", SearchParamUtil.getString(searchParam, IP_ADDRESS))
                .setParameter("actionBy", SearchParamUtil.getString(searchParam, ACTION_BY))
                .getSingleResult();
    }

    @Override
    public List<ActionLog> getAll(SearchParam searchParam) {
        return em.createQuery(
                        "select a " +
                                "from ActionLog a " +
                                "join fetch a.actionBy admin " +
                                "where " +
                                "(:remarks is null or a.remarks like concat('%', :remarks, '%')) and " +
                                "(:targetType is null or a.targetType = :targetType) and " +
                                "(:actionType is null or a.actionType = :actionType) and " +
                                "(:ipAddress is null or a.ipAddress like concat('%', :ipAddress, '%')) and " +
                                "(:actionBy is null or admin.email like concat('%', :actionBy, '%'))"
                        , ActionLog.class)
                .setParameter("remarks", SearchParamUtil.getString(searchParam, REMARKS))
                .setParameter("targetType", SearchParamUtil.getString(searchParam, TARGET_TYPE))
                .setParameter("actionType", SearchParamUtil.getString(searchParam, ACTION_TYPE))
                .setParameter("ipAddress", SearchParamUtil.getString(searchParam, IP_ADDRESS))
                .setParameter("actionBy", SearchParamUtil.getString(searchParam, ACTION_BY))
                .getResultList();
    }
}
