package com.tansen.administrative.complaints.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import com.tansen.common.dto.SearchParam;
import com.tansen.administrative.complaints.dto.ComplaintUniqueDto;
import com.tansen.administrative.complaints.dto.request.ComplaintAssignRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;

public interface ComplaintService {
    ApiResponse<?> listComplains(SearchParam searchParam, Principal loggedInAdmin) throws JsonProcessingException;

    ApiResponse<?> assignComplaintToAuthorityUser(ComplaintAssignRequest complaintAssignRequest, Principal loggedInAdmin);

    ApiResponse<?> getComplaint(ComplaintUniqueDto complaintUniqueIdDto, Principal loggedInAdmin);

    ApiResponse<?> closedComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin, HttpServletRequest httpServletRequest);
    ApiResponse<?> inProgressComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin);
    ApiResponse<?>  listAssignTo(SearchParam searchParam, Principal loggedInUser);
    ApiResponse<?> resolveComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin);
    ApiResponse<?> rejectComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin);


}
