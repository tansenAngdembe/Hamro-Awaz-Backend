package com.tansen.government.slarules.service;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.complaints.dto.request.ComplaintAssignRequest;
import com.tansen.government.slarules.dto.CreateEscalationRequest;

import java.security.Principal;

public interface EscalationService {
    ApiResponse<?> listEscalation(SearchParam searchParam, Principal loggedInAdmin);
    ApiResponse<?> createEscalation(CreateEscalationRequest escalationRequest, Principal loggedInAdmin);


}
