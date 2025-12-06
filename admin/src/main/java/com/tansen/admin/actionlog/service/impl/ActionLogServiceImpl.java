package com.tansen.admin.actionlog.service.impl;

import com.tansen.admin.actionlog.dto.ActionLogModel;
import com.tansen.admin.actionlog.dto.ListActionLogResponse;
import com.tansen.admin.actionlog.mapper.AdminActionLogMapper;
import com.tansen.admin.actionlog.service.ActionLogService;
import com.tansen.common.dto.*;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.ActionLog;
import com.tansen.repository.ActionLogsRepository;
import com.tansen.repository.AdminRepository;
import com.tansen.repository.searchrepo.AdminActionLogSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class ActionLogServiceImpl implements ActionLogService {
    private static final Logger LOG = LoggerFactory.getLogger(ActionLogServiceImpl.class);

    private final AdminRepository adminRepository;
    private final ActionLogsRepository actionLogRepository;
    private final AdminActionLogSearchRepository actionLogSearchRepository;
    private final AdminActionLogMapper adminActionLogMapper;
    private final SearchResponse searchResponse;


    public ActionLogServiceImpl(AdminRepository adminRepository, ActionLogsRepository actionLogRepository, AdminActionLogSearchRepository actionLogSearchRepository,  AdminActionLogMapper adminActionLogMapper, SearchResponse searchResponse) {
        this.adminRepository = adminRepository;
        this.actionLogRepository = actionLogRepository;
        this.actionLogSearchRepository = actionLogSearchRepository;
        this.adminActionLogMapper = adminActionLogMapper;
        this.searchResponse = searchResponse;
    }

    @Override
    public void insertActionLog(ActionLogModel actionLogModel, Principal loggedInAdmin) {
        ActionLog actionLog = new ActionLog();
        actionLog.setRemarks(actionLogModel.getRemarks());
        actionLog.setTargetType(actionLogModel.getTargetType());
        actionLog.setTargetId(actionLogModel.getTargetId());
        actionLog.setActionType(actionLogModel.getActionType());
        actionLog.setActionBy(adminRepository.findByEmail(loggedInAdmin.getName()));
        actionLog.setActionDate(LocalDateTime.now());
        actionLog.setIpAddress(actionLogModel.getIpAddress());
        LOG.info("Action Log inserted: Remarks={}, TargetType={}, TargetId={}, ActionBy={}, IpAddress={}",
                actionLog.getRemarks(),
                actionLog.getTargetType(),
                actionLog.getTargetId(),
                actionLog.getActionBy().getId(),
                actionLog.getIpAddress()
                );
        actionLogRepository.save(actionLog);
    }



    @Override
    public ApiResponse<?> listActionLogs(SearchParam searchParam) {
        SearchResponseWithMapperBuilder<ActionLog, ListActionLogResponse> responseBuilder =
                SearchResponseWithMapperBuilder.<ActionLog, ListActionLogResponse>builder()
                        .count(actionLogSearchRepository::count)
                        .searchData(actionLogSearchRepository::getAll)
                        .mapperFunction(this.adminActionLogMapper::listActionLogRes)
                        .searchParam(searchParam)
                        .build();
        PageableResponse<ListActionLogResponse> response=searchResponse.getSearchResponse(responseBuilder);
        LOG.info("ActionLog  listed successfully for user ");
        return ResponseUtil.getSuccessfulApiResponse(response,"Action Log listed successfully");
    }
}
