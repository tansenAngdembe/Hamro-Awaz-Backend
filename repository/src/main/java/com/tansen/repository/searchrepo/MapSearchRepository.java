package com.tansen.repository.searchrepo;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.repo.SearchRepository;
import com.tansen.entity.ComplaintCoordinates;

import java.util.List;

public interface MapSearchRepository extends SearchRepository<ComplaintCoordinates> {
    Long count(SearchParam searchParam, Long municipalityId);

    List<ComplaintCoordinates> getAll(SearchParam searchParam, Long municipalityId);
}
