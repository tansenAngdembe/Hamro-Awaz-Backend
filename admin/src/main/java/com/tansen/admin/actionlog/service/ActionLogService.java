package com.tansen.admin.actionlog.service;

import com.tansen.admin.actionlog.dto.ActionLogModel;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;

import java.security.Principal;

public interface ActionLogService {
    void insertActionLog(ActionLogModel actionLogModel, Principal loggedInAdmin);
    ApiResponse<?> listActionLogs(SearchParam searchParam);
}
