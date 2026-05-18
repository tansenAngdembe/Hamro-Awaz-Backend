package com.tansen.admin.complaints.controller;

import com.tansen.admin.complaints.dto.ComplaintUniqueDto;
import com.tansen.admin.complaints.service.ComplaintService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import com.tansen.common.dto.SearchParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.ADMIN_API + ApiConstant.SLASH + ApiConstant.COMPLAINT)
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }
    @PostMapping(ApiConstant.VIEW)
    @PreAuthorize("hasAuthority('VIEW_COMPLAINTS')")
    public ApiResponse<?> getComplaint(@RequestBody ComplaintUniqueDto complaintUniqueIdDto, Principal principal) {
        return complaintService.getComplaint(complaintUniqueIdDto, principal);
    }

    @PostMapping(ApiConstant.CLOSED)
    @PreAuthorize("hasAuthority('CLOSE_COMPLAINT')")
    public ApiResponse<?> closedComplaint(@RequestBody ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin, HttpServletRequest httpServletRequest) {
        return complaintService.closedComplaint(complaintUniqueIdDto, loggedInAdmin, httpServletRequest);
    }

    @PostMapping(ApiConstant.INPROGRESS)
    @PreAuthorize("hasAuthority('UPDATE_COMPLAINT_STATUS')")
    public ApiResponse<?> inProgressComplaint(@RequestBody ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin) {
        return complaintService.inProgressComplaint(complaintUniqueIdDto, loggedInAdmin);
    }

    @PostMapping(ApiConstant.RESOLVE)
    @PreAuthorize("hasAuthority('UPDATE_COMPLAINT_STATUS')")
    public  ApiResponse<?> resolveComplaint(@RequestBody  ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin, HttpServletRequest httpServletRequest){
        return complaintService.resolveComplaint(complaintUniqueIdDto, loggedInAdmin, httpServletRequest);
    }
    @PostMapping(ApiConstant.REJECT)
    @PreAuthorize("hasAuthority('REJECT_COMPLAINT')")
    public ApiResponse<?> rejectComplaint(@RequestBody ComplaintUniqueIdDto complaintUniqueIdDto, Principal loggedInAdmin){
        return complaintService.rejectComplaint(complaintUniqueIdDto, loggedInAdmin);
    }
    @PostMapping(ApiConstant.LIST)
    public  ApiResponse<?> listComplains(SearchParam searchParam){
        return complaintService.listComplains(searchParam);
    }


}
