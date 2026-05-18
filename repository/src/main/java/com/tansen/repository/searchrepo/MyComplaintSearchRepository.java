package com.tansen.repository.searchrepo;

import com.tansen.common.dto.SearchParam;
import com.tansen.entity.Complaint;

import java.util.List;

public interface MyComplaintSearchRepository {
    Long count(SearchParam searchParam, Long userId);
    List<Complaint> getAll(SearchParam searchParam, Long userId);
}