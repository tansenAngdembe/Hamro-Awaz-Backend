package com.tansen.admin.dashboard.utill;

import com.tansen.admin.dashboard.enums.ReportPeriod;
import com.tansen.admin.dashboard.service.impl.ReportServiceImpl;
import com.tansen.admin.dashboard.utill.service.ComplaintReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ComplaintReportScheduler {

    private final ComplaintReportService reportService;

    // Runs every day at 00:10 AM
//    @Scheduled(cron = "0 10 0 * * *")
    @Scheduled(fixedRate = 3 * 60 * 1000)
    public void generateDailyReport() {
        log.info("Starting daily complaint report generation...");
        reportService.generateReport(ReportPeriod.DAILY);
        log.info("Daily complaint report completed.");
    }
}