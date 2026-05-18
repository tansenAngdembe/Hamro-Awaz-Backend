package com.tansen.admin.dashboard.service.impl;

import com.tansen.admin.dashboard.dto.ReportSummaryResponse;
import com.tansen.admin.dashboard.service.ReportService;
import com.tansen.common.dto.ApiResponse;
import com.tansen.entity.AuthorityUser;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ReportServiceImpl implements ReportService {

//    @Override
//    public ApiResponse<?> getSummary(Principal principal) {
//        try {
//            AuthorityUser admin = resolveAdmin(principal);
//            Long municipalityId = getMunicipalityId(admin);
//
//            // Current month window
//            LocalDateTime currentStart = LocalDate.now().withDayOfMonth(1).atStartOfDay();
//            LocalDateTime currentEnd   = LocalDateTime.now();
//
//            // Previous month window
//            LocalDateTime prevStart = currentStart.minusMonths(1);
//            LocalDateTime prevEnd   = currentStart.minusSeconds(1);
//
//            long curTotal      = reportRepository.countTotalByDateRange(municipalityId, currentStart, currentEnd);
//            long curResolved   = reportRepository.countByStatusAndDateRange(municipalityId, "RESOLVED",    currentStart, currentEnd);
//            long curInProgress = reportRepository.countByStatusAndDateRange(municipalityId, "IN_PROGRESS", currentStart, currentEnd);
//            long curEscalated  = reportRepository.countByStatusAndDateRange(municipalityId, "ESCALATED",   currentStart, currentEnd);
//            long curPending    = reportRepository.countByStatusAndDateRange(municipalityId, "PENDING",     currentStart, currentEnd);
//
//            long prevTotal      = reportRepository.countTotalByDateRange(municipalityId, prevStart, prevEnd);
//            long prevResolved   = reportRepository.countByStatusAndDateRange(municipalityId, "RESOLVED",    prevStart, prevEnd);
//            long prevInProgress = reportRepository.countByStatusAndDateRange(municipalityId, "IN_PROGRESS", prevStart, prevEnd);
//            long prevEscalated  = reportRepository.countByStatusAndDateRange(municipalityId, "ESCALATED",   prevStart, prevEnd);
//
//            ReportSummaryResponse summary = ReportSummaryResponse.builder()
//                    .totalComplaints(curTotal)
//                    .resolved(curResolved)
//                    .pending(curPending)
//                    .inProgress(curInProgress)
//                    .escalated(curEscalated)
//                    .totalChangePercent(percentChange(prevTotal,      curTotal))
//                    .resolvedChangePercent(percentChange(prevResolved,   curResolved))
//                    .inProgressChangePercent(percentChange(prevInProgress, curInProgress))
//                    .escalatedChangePercent(percentChange(prevEscalated,  curEscalated))
//                    .build();
//
//            return ResponseUtil.getSuccessfulApiResponse(summary, "Summary fetched.");
//        } catch (Exception e) {
//            LOG.error("Failed to get summary", e);
//            return ResponseUtil.getFailureResponse("Failed to fetch summary: " + e.getMessage());
//        }
//    }

}
