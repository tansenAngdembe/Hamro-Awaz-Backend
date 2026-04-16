package com.tansen.repository.searchrepo.impl;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.utility.SearchParamUtil;
import com.tansen.entity.Municipality;
import com.tansen.entity.User;
import com.tansen.repository.searchrepo.UserSearchTestRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.tansen.common.constant.SearchParamConstant.*;

@Repository
@RequiredArgsConstructor
public class UserSearchTestRepositoryImpl implements UserSearchTestRepository {


        @PersistenceContext
        protected EntityManager em;

        private static final String BASE_WHERE =
                "(:fullName is null or u.fullName like CONCAT('%', :fullName, '%')) and " +
                        "(:uniqueId is null or u.uniqueId like CONCAT('%', :uniqueId, '%')) and " +
                        "(:email is null or u.email like CONCAT('%', :email, '%')) and " +
                        "(:phoneNumber is null or u.phoneNumber like CONCAT('%', :phoneNumber, '%')) and " +
                        "(:address is null or u.address like CONCAT('%', :address, '%')) and " +
                        "(:role is null or r.name = :role) and " +
                        "(:status is null or s.description = :status) and " +
                        "(:isActive is null or u.isActive = :isActive) and " +
                        "(:isUserVerified is null or u.isUserVerified = :isUserVerified) and " +
                        "(:municipalityId is null or u.municipality.id = :municipalityId)";

        private static final String BASE_FROM =
                "from User u " +
                        "join Status s on s.id = u.status.id " +
                        "join Role r on r.id = u.role.id " +
                        "where ";

        @Override
        public Long count(SearchParam searchParam, Municipality municipality) {
            return (Long) buildQuery(
                    "select COUNT(u.id) " + BASE_FROM + BASE_WHERE,
                    searchParam,
                    municipality,
                    Long.class
            ).getSingleResult();
        }

        @Override
        public List<User> getAll(SearchParam searchParam, Municipality municipality) {
            return buildQuery(
                    "select u " + BASE_FROM + BASE_WHERE,
                    searchParam,
                    municipality,
                    User.class
            )
                    .setFirstResult(searchParam.getFirstRow())
                    .setMaxResults(searchParam.getPageSize())
                    .getResultList();
        }

        private <T> TypedQuery<T> buildQuery(String jpql, SearchParam searchParam, Municipality municipality, Class<T> resultClass) {
            return em.createQuery(jpql, resultClass)
                    .setParameter("fullName",        SearchParamUtil.getString(searchParam, FULL_NAME))
                    .setParameter("uniqueId",        SearchParamUtil.getString(searchParam, UNIQUE_ID))
                    .setParameter("email",           SearchParamUtil.getString(searchParam, EMAIL))
                    .setParameter("phoneNumber",     SearchParamUtil.getString(searchParam, PHONE_NUMBER))
                    .setParameter("address",         SearchParamUtil.getString(searchParam, ADDRESS))
                    .setParameter("role",            SearchParamUtil.getString(searchParam, ROLE))
                    .setParameter("status",          SearchParamUtil.getString(searchParam, STATUS))
                    .setParameter("isActive",        SearchParamUtil.getBoolean(searchParam, IS_ACTIVE))
                    .setParameter("isUserVerified",  SearchParamUtil.getBoolean(searchParam, IS_USER_VERIFIED))
                    .setParameter("municipalityId",  municipality != null ? municipality.getId() : null);
        }

}
