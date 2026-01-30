package com.tansen.government.map.controller;

import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.complaints.dto.request.ComplaintAssignRequest;
import com.tansen.government.complaints.service.ComplaintService;
import com.tansen.government.map.service.MapService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.MUNICIPALITY_API + ApiConstant.SLASH + ApiConstant.COMPLAINT)
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @PostMapping(ApiConstant.LIST)
    @PreAuthorize("hasAuthority('VIEW_COMPLAINTS')")
    public ApiResponse<?> listComplaintCoordinates(SearchParam searchParam, Principal loggedInAdmin) {
        return mapService.listComplaintCoordinates(searchParam, loggedInAdmin);
    }

}
