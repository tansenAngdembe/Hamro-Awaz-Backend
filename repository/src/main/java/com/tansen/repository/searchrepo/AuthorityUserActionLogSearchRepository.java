package com.tansen.repository.searchrepo;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.repo.SearchRepository;
import com.tansen.entity.AuthorityUserActionLog;

import java.util.List;

public interface AuthorityUserActionLogSearchRepository extends SearchRepository<AuthorityUserActionLog> {
    Long count(SearchParam searchParam, String username);
    List<AuthorityUserActionLog> getAll(SearchParam searchParam, String username);
}
