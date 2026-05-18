package com.tansen.administrative.map.service;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;

import java.security.Principal;

public interface MapService {
    ApiResponse<?> listComplaintCoordinates(SearchParam searchParam, Principal loggedInAdmin);





}
