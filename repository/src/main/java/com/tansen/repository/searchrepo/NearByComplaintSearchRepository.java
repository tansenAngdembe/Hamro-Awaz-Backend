package com.tansen.repository.searchrepo;

import com.tansen.common.dto.SearchParam;
import com.tansen.entity.Complaint;

import java.math.BigDecimal;
import java.util.List;

public interface NearByComplaintSearchRepository {
    Long countNearby( BigDecimal latitude, BigDecimal longitude, Double radiusKm,Long statusId, Long categoryId);
    List<Complaint> findNearby( BigDecimal latitude, BigDecimal longitude, Double radiusKm,Long statusId, Long categoryId);
}
