package com.tansen.government.map.service;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.complaints.dto.request.ComplaintAssignRequest;

import java.security.Principal;

public interface MapService {
    ApiResponse<?> listComplaintCoordinates(SearchParam searchParam, Principal loggedInAdmin);





}
