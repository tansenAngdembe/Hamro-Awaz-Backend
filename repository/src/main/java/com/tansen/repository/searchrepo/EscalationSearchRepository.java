package com.tansen.repository.searchrepo;

import com.tansen.common.dto.SearchParam;
import com.tansen.common.repo.SearchRepository;
import com.tansen.entity.Complaint;
import com.tansen.entity.Escalation;

import java.util.List;

public interface EscalationSearchRepository extends SearchRepository<Escalation> {
    Long count(SearchParam searchParam, Long municipalityId);

    List<Escalation> getAll(SearchParam searchParam, Long municipalityId);
}
