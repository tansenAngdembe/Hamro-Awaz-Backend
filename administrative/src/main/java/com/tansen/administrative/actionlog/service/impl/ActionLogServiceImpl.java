package com.tansen.administrative.actionlog.service.impl;

import com.tansen.common.dto.*;
import com.tansen.common.dto.model.ActionLogModel;
import com.tansen.common.service.SearchResponse;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.AuthorityUserActionLog;
import com.tansen.administrative.actionlog.dto.ListActionLogResponse;
import com.tansen.administrative.actionlog.mapper.MunicipalityActionLogMapper;
import com.tansen.administrative.actionlog.service.ActionLogService;
import com.tansen.repository.AuthorityUserActionLogRepository;
import com.tansen.repository.AuthorityUserRepository;
import com.tansen.repository.searchrepo.AuthorityUserActionLogSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ActionLogServiceImpl implements ActionLogService {
    private static final Logger LOG = LoggerFactory.getLogger(ActionLogServiceImpl.class);

    private final AuthorityUserRepository authorityUserRepository;
    private final AuthorityUserActionLogRepository authorityUserActionLogRepository;
    private final AuthorityUserActionLogSearchRepository authorityUserActionLogSearchRepository;
    private final MunicipalityActionLogMapper vendorActionLogMapper;
    private final SearchResponse searchResponse;

    public ActionLogServiceImpl(AuthorityUserRepository authorityUserRepository, AuthorityUserActionLogRepository vendorUserActionLogRepository, AuthorityUserActionLogSearchRepository authorityUserActionLogSearchRepository, MunicipalityActionLogMapper vendorActionLogMapper, SearchResponse searchResponse) {
        this.authorityUserRepository = authorityUserRepository;
        this.authorityUserActionLogRepository = vendorUserActionLogRepository;
        this.authorityUserActionLogSearchRepository = authorityUserActionLogSearchRepository;
        this.vendorActionLogMapper = vendorActionLogMapper;
        this.searchResponse = searchResponse;
    }

    @Override
    public void insertActionLog(ActionLogModel actionLogModel, Principal loggedInAdmin) {
        Optional<AuthorityUser> vendorUserOptional = authorityUserRepository.findByEmail(loggedInAdmin.getName());
        if (vendorUserOptional.isEmpty()) {
            LOG.error("User with email {} not found", loggedInAdmin.getName());
            return;
        }
        AuthorityUser vendorUser = vendorUserOptional.get();
        AuthorityUserActionLog actionLog = new AuthorityUserActionLog();
        actionLog.setRemarks(actionLogModel.getRemarks());
        actionLog.setTargetType(actionLogModel.getTargetType());
        actionLog.setTargetId(actionLogModel.getTargetId());
        actionLog.setActionType(actionLogModel.getActionType());
        actionLog.setActionBy(vendorUser);
        actionLog.setActionDate(LocalDateTime.now());
        actionLog.setIpAddress(actionLogModel.getIpAddress());
        LOG.info("Action Log inserted: Remarks={}, TargetType={}, TargetId={}, ActionBy={}, IpAddress={}",
                actionLog.getRemarks(),
                actionLog.getTargetType(),
                actionLog.getTargetId(),
                actionLog.getActionBy().getId(),
                actionLog.getIpAddress()
                );
        authorityUserActionLogRepository.save(actionLog);
    }

    @Override
    public ApiResponse<?> listActionLogs(SearchParam searchParam, Principal loggedInVendor) {
        AuthorityUser vendorUser = authorityUserRepository.findByEmail(loggedInVendor.getName()).orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + loggedInVendor.getName())
        );
        Long vendorId = vendorUser.getMunicipality().getId();
        SearchResponseWithMapperBuilder<AuthorityUserActionLog, ListActionLogResponse> responseBuilder =
                SearchResponseWithMapperBuilder.<AuthorityUserActionLog, ListActionLogResponse>builder()
                        .count(sp-> authorityUserActionLogSearchRepository.count(searchParam, String.valueOf(vendorId)))
                        .searchData( p-> authorityUserActionLogSearchRepository.getAll(searchParam, String.valueOf(vendorId)))
                        .mapperFunction(this.vendorActionLogMapper::listActionLogRes)
                        .searchParam(searchParam)
                        .build();
        PageableResponse<ListActionLogResponse> response=searchResponse.getSearchResponse(responseBuilder);
        LOG.info("ActionLog listed successfully for vendorId {}", vendorId);
        return ResponseUtil.getSuccessfulApiResponse(response,"ActionLog listed successfully");
    }
}

