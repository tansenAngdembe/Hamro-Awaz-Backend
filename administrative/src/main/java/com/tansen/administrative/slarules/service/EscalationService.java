package com.tansen.administrative.slarules.service;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import com.tansen.administrative.slarules.dto.CreateEscalationRequest;

import java.security.Principal;

public interface EscalationService {
    ApiResponse<?> listEscalation(SearchParam searchParam, Principal loggedInAdmin);
    ApiResponse<?> createEscalation(CreateEscalationRequest escalationRequest, Principal loggedInAdmin);


}
