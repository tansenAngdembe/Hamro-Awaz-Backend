package com.tansen.repository.searchrepo;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.repo.SearchRepository;
import com.tansen.entity.Complaint;

import java.util.List;

public interface ComplaintSearchRepository extends SearchRepository<Complaint> {
    Long count(SearchParam searchParam, Long municipalityId);

    List<Complaint> getAll(SearchParam searchParam, Long municipalityId);
}
