package com.tansen.repository.searchrepo;

import com.tansen.common.dto.SearchParam;
import com.tansen.entity.Municipality;
import com.tansen.entity.User;

import java.util.List;

public interface UserSearchTestRepository {
        Long count(SearchParam searchParam, Municipality municipality);
        List<User> getAll(SearchParam searchParam, Municipality municipality);
}
