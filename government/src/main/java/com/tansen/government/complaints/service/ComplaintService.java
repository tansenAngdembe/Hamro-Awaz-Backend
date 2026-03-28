package com.tansen.government.complaints.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.complaints.dto.request.ComplaintAssignRequest;

import java.security.Principal;

public interface ComplaintService {
    ApiResponse<?> listComplains(SearchParam searchParam, Principal loggedInAdmin) throws JsonProcessingException;

    ApiResponse<?> assignComplaintToAuthorityUser(ComplaintAssignRequest complaintAssignRequest, Principal loggedInAdmin);

    ApiResponse<?> getComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin);

    ApiResponse<?> closedComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin);
    ApiResponse<?> inProgressComplaint(ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin);
    ApiResponse<?>  listAssignTo(SearchParam searchParam, Principal loggedInUser);


}
