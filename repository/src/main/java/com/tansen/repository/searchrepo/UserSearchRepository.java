package com.tansen.repository.searchrepo;

import com.tansen.common.dto.SearchParam;
import com.tansen.entity.User;

import java.util.List;

public interface UserSearchRepository {
    Long count(SearchParam searchParam);
    List<User> getAll(SearchParam searchParam);
}
