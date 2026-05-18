package com.tansen.administrative.actionlog.service;


import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import com.tansen.common.dto.model.ActionLogModel;

import java.security.Principal;

public interface ActionLogService {
    void insertActionLog(ActionLogModel actionLogModel, Principal loggedInAdmin);
    ApiResponse<?> listActionLogs(SearchParam searchParam, Principal loggedInAdmin);
}
