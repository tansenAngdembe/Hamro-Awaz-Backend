package com.tansen.repository.searchrepo;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.repo.SearchRepository;
import com.tansen.entity.Category;

import java.util.List;

public interface CategorySearchRepository extends SearchRepository<Category> {
    Long count(SearchParam searchParam,Long municipalityId);
    List<Category> getAll(SearchParam searchParam, Long municipalityId);
}
