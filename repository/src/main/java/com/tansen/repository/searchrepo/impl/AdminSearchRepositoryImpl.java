package com.tansen.repository.searchrepo.impl;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.utility.SearchParamUtil;
import com.tansen.entity.Admin;
import com.tansen.repository.searchrepo.AdminSearchRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tansen.common.constant.SearchParamConstant.*;

@Repository
@RequiredArgsConstructor
public class AdminSearchRepositoryImpl implements AdminSearchRepository {
    @PersistenceContext
    protected EntityManager em;

    @Override
    public Long count(SearchParam searchParam) {
        return (Long) em.createQuery("select COUNT(a.id) " +
                        "from Admin  a " +
                        "join Status s on s.id=a.status.id " +
                        "join a.accessGroup ag"+
                        " where " +
                        "(:accessGroup is null or ag.name = :accessGroup) and " +
                        "(:name is null or a.name like CONCAT('%', :name, '%')) and " +
                        "(:email is null or a.email like CONCAT('%', :email, '%')) and " +
                        "(:mobileNumber is null or a.mobileNumber like CONCAT('%', :mobileNumber, '%')) and " +
                        "(:status is null or s.description=:status) ")
                .setParameter("mobileNumber", SearchParamUtil.getString(searchParam,MOBILE_NUMBER))
                .setParameter("email",SearchParamUtil.getString(searchParam,EMAIL))
                .setParameter("accessGroup",SearchParamUtil.getString(searchParam,ACCESS_GROUP))
                .setParameter("name", SearchParamUtil.getString(searchParam, NAME))
                .setParameter("status", SearchParamUtil.getString(searchParam, STATUS))
                .getSingleResult();
    }

    @Override
    public List<Admin> getAll(SearchParam searchParam) {
        return em.createQuery("select a " +
                        "from Admin  a " +
                        "join Status s on s.id=a.status.id " +
                        "join a.accessGroup ag"+
                        " where " +
                        "(:accessGroup is null or ag.name = :accessGroup) and " +
                        "(:name is null or a.name like CONCAT('%', :name, '%')) and " +
                        "(:email is null or a.email like CONCAT('%', :email, '%')) and " +
                        "(:username is null or a.username like CONCAT('%', :username, '%')) and " +
                        "(:mobileNumber is null or a.mobileNumber like CONCAT('%', :mobileNumber, '%')) and " +
                        "(:status is null or s.description=:status)",Admin.class)
                .setParameter("mobileNumber",SearchParamUtil.getString(searchParam,MOBILE_NUMBER))
                .setParameter("email",SearchParamUtil.getString(searchParam,EMAIL))
                .setParameter("accessGroup",SearchParamUtil.getString(searchParam,ACCESS_GROUP))
                .setParameter("name", SearchParamUtil.getString(searchParam, NAME))
                .setParameter("status", SearchParamUtil.getString(searchParam, STATUS))
                .setParameter("username", SearchParamUtil.getString(searchParam,USERNAME))
                .setFirstResult(searchParam.getFirstRow())
                .setMaxResults(searchParam.getPageSize())
                .getResultList();
    }
}
