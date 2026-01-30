package com.tansen.government.slarules.controller;

import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.slarules.dto.CreateEscalationRequest;
import com.tansen.government.slarules.service.EscalationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.MUNICIPALITY_API + ApiConstant.SLASH + ApiConstant.ESCALATION)
public class EscalationController {
    private final EscalationService escalationService;

    public EscalationController(EscalationService escalationService) {
        this.escalationService = escalationService;
    }
//    Escalation is the process of transferring an
//    issue or support ticket to a higher level of authority
//    or specialized expertise when the initial support agent
//    cannot resolve it within a specified timeframe.
//    Escalations are designed to ensure Service Level Agreement (SLA)
//    compliance and prevent bottlenecks, ensuring critical problems are
//    prioritized and resolved quickly.



    @PostMapping(ApiConstant.LIST)
    @PreAuthorize("hasAuthority('VIEW_SLA_RULES')")
    public ApiResponse<?> listComplains(SearchParam searchParam, Principal loggedInAdmin) {
        return escalationService.listEscalation(searchParam, loggedInAdmin);
    }
    @PostMapping(ApiConstant.CREATE)
    @PreAuthorize("hasAuthority('CREATE_SLA_RULE')")
    public ApiResponse<?> createEscalation(@RequestBody CreateEscalationRequest escalationRequest, Principal loggedInAdmin){
        return escalationService.createEscalation(escalationRequest, loggedInAdmin);
    }


}
