package com.tansen.admin.complaints.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tansen.admin.complaints.dto.ComplaintUniqueDto;
import com.tansen.admin.complaints.dto.request.ComplaintAssignRequest;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import com.tansen.common.dto.SearchParam;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;

public interface ComplaintService {
    ApiResponse<?> listComplains(SearchParam searchParam);


    ApiResponse<?> getComplaint(ComplaintUniqueDto complaintUniqueIdDto, Principal loggedInAdmin);

    ApiResponse<?> closedComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin, HttpServletRequest httpServletRequest);
    ApiResponse<?> inProgressComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin);
    ApiResponse<?> resolveComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin, HttpServletRequest  httpServletRequest);
    ApiResponse<?> rejectComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin);


}
