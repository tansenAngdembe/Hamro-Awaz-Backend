package com.tansen.government.complaints.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.complaints.dto.ComplaintUniqueDto;
import com.tansen.government.complaints.dto.request.ComplaintAssignRequest;
import com.tansen.government.complaints.service.ComplaintService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.MUNICIPALITY_API + ApiConstant.SLASH + ApiConstant.COMPLAINT)
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping(ApiConstant.LIST)
    @PreAuthorize("hasAuthority('VIEW_COMPLAINTS')")
    public ApiResponse<?> listComplains(SearchParam searchParam, Principal loggedInAdmin) throws JsonProcessingException {
        return complaintService.listComplains(searchParam, loggedInAdmin);
    }
    @PostMapping(ApiConstant.VIEW)
    @PreAuthorize("hasAuthority('VIEW_COMPLAINTS')")
    public ApiResponse<?> getComplaint(@RequestBody ComplaintUniqueDto complaintUniqueIdDto, Principal principal) {
        return complaintService.getComplaint(complaintUniqueIdDto, principal);
    }
    @PostMapping(ApiConstant.CLOSED)
    @PreAuthorize("hasAuthority('CLOSE_COMPLAINT')")
    public ApiResponse<?> closedComplaint(@RequestBody ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin,HttpServletRequest httpServletRequest) {
        return complaintService.closedComplaint(complaintUniqueIdDto, loggedInAdmin, httpServletRequest);
    }

    @PostMapping(ApiConstant.ASSIGNCOMPLAINT)
    @PreAuthorize("hasAuthority('ASSIGN_COMPLAINT')")
    public ApiResponse<?> assignComplaintToAuthorityUser(@RequestBody ComplaintAssignRequest complaintAssignRequest, Principal loggedInAdmin) {
        return complaintService.assignComplaintToAuthorityUser(complaintAssignRequest, loggedInAdmin);
    }

    @PostMapping(ApiConstant.INPROGRESS)
    @PreAuthorize("hasAuthority('UPDATE_COMPLAINT_STATUS')")
    public ApiResponse<?> inProgressComplaint(@RequestBody ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin) {
        return complaintService.inProgressComplaint(complaintUniqueIdDto, loggedInAdmin);
    }
    @PostMapping(ApiConstant.ASSIGNTO + ApiConstant.SLASH + ApiConstant.LIST)
    @PreAuthorize("hasAuthority('VIEW_STAFF')")
    public ApiResponse<?> listAssignTo(@RequestBody @Valid SearchParam searchParam, Principal loggedInUser) {
        return complaintService.listAssignTo(searchParam, loggedInUser);
    }
    @PostMapping(ApiConstant.RESOLVE)
    @PreAuthorize("hasAuthority('UPDATE_COMPLAINT_STATUS')")
    public  ApiResponse<?> resolveComplaint(@RequestBody  ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin){
        return complaintService.resolveComplaint(complaintUniqueIdDto, loggedInAdmin);
   }
    @PostMapping(ApiConstant.REJECT)
    @PreAuthorize("hasAuthority('REJECT_COMPLAINT')")
    public ApiResponse<?> rejectComplaint(@RequestBody ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin){
        return complaintService.rejectComplaint(complaintUniqueIdDto, loggedInAdmin);
   }


}
