package com.tansen.admin.actionlog.controller;


import com.tansen.admin.actionlog.service.ActionLogService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstant.ADMIN_API)
public class ActionLogController {

    private final ActionLogService actionLogService;

    public ActionLogController(ActionLogService actionLogService) {
        this.actionLogService = actionLogService;
    }
    @PostMapping(ApiConstant.ACTION_LOG+ApiConstant.SLASH+ApiConstant.LIST)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ApiResponse<?> listActionLog(@RequestBody SearchParam searchParam) {
        return actionLogService.listActionLogs(searchParam);
    }
}
