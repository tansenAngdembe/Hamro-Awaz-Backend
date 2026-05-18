package com.tansen.repository.searchrepo;

import com.tansen.common.dto.SearchParam;
import com.tansen.entity.AdministrativeUnit;
import com.tansen.entity.User;

import java.util.List;

public interface UserSearchTestRepository {
        Long count(SearchParam searchParam, AdministrativeUnit municipality);
        List<User> getAll(SearchParam searchParam, AdministrativeUnit municipality);
}
