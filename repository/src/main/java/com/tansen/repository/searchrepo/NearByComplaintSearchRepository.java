package com.tansen.repository.searchrepo;

import com.tansen.common.dto.SearchParam;
import com.tansen.entity.Complaint;

import java.math.BigDecimal;
import java.util.List;

public interface NearByComplaintSearchRepository {
    Long countNearby(SearchParam searchParam, BigDecimal latitude, BigDecimal longitude, Double radiusKm);
    List<Complaint> findNearby(SearchParam searchParam, BigDecimal latitude, BigDecimal longitude, Double radiusKm);
}
