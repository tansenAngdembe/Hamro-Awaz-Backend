package com.tansen.repository.searchrepo;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.repo.SearchRepository;
import com.tansen.entity.AuthorityUser;

import java.util.List;

public interface AuthorityUserSearchRepository extends SearchRepository<AuthorityUser> {
    List<AuthorityUser> getAll(SearchParam searchParam);
}
