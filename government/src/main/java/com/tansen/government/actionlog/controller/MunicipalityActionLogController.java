package com.tansen.government.actionlog.controller;

import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.actionlog.service.ActionLogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.MUNICIPALITY_API)
public class MunicipalityActionLogController {

    private final ActionLogService actionLogService;


    public MunicipalityActionLogController(ActionLogService actionLogService) {
        this.actionLogService = actionLogService;
    }

    @PostMapping(ApiConstant.ACTION_LOG+ApiConstant.SLASH+ApiConstant.LIST)
    public ApiResponse<?> listActionLogs(@RequestBody SearchParam searchParam, Principal loggedInVendor) {
        return actionLogService.listActionLogs(searchParam, loggedInVendor);
    }
}
