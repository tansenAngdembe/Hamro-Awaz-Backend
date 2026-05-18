package com.tansen.admin.dashboard.utill.service;

import com.tansen.admin.dashboard.enums.ReportPeriod;
import com.tansen.common.dto.ComplaintStats;
import com.tansen.common.dto.EmptyComplaintStats;
import com.tansen.common.exception.ResourceNotFoundException;
import com.tansen.entity.AdministrativeUnit;
import com.tansen.entity.ComplaintReport;
import com.tansen.repository.AdministrativeUnitRepository;
import com.tansen.repository.ComplaintReportRepository;
import com.tansen.repository.ComplaintRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ComplaintReportService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintReportRepository reportRepository;
    private final AdministrativeUnitRepository administrativeUnitRepository;

    public void generateReport(ReportPeriod period) {

        LocalDateTime toDate = LocalDateTime.now();
        LocalDateTime fromDate = switch (period) {
            case DAILY -> toDate.minusDays(1);
            case WEEKLY -> toDate.minusWeeks(1);
            case MONTHLY -> toDate.minusMonths(1);
        };

        //  Validate administrative units exist
        List<AdministrativeUnit> units = administrativeUnitRepository.findAll();

        if (units == null || units.isEmpty()) {
            log.error("No Administrative Units found. Report generation aborted.");
            throw new ResourceNotFoundException("No Administrative Units found for report generation");
        }

        // Loop per unit (IMPORTANT FIX)
        for (AdministrativeUnit unit : units) {

            try {

                // ✅ 2. Fetch stats safely
                ComplaintStats stats = complaintRepository.fetchStats(
                        fromDate, toDate, unit.getId()
                );

                if (stats == null) {
                    log.warn("No complaint stats found for unit: {}", unit.getId());
                    continue;
                }

                ComplaintStats previousStats = complaintRepository.fetchStats(
                        fromDate.minusDays(getPreviousOffset(period)),
                        fromDate,
                        unit.getId()
                );

                if (previousStats == null) {
                    previousStats = new EmptyComplaintStats(); // fallback object
                }


                // ✅ 3. Build report
                ComplaintReport report = new ComplaintReport();
                report.setFromDate(fromDate.toLocalDate());
                report.setToDate(toDate.toLocalDate());
                report.setAdministrative(unit);

                report.setTotalComplaints(stats.getTotal());
                report.setResolved(stats.getResolved());
                report.setPending(stats.getPending());
                report.setInProgress(stats.getInProgress());
                report.setEscalated(stats.getEscalated());




                report.setTotalChangePercent(
                        calculateChange(stats.getTotal(), previousStats.getTotal())
                );
                report.setResolvedChangePercent(
                        calculateChange(stats.getResolved(), previousStats.getResolved())
                );
                report.setInProgressChangePercent(
                        calculateChange(stats.getInProgress(), previousStats.getInProgress())
                );
                report.setEscalatedChangePercent(
                        calculateChange(stats.getEscalated(), previousStats.getEscalated())
                );

                report.setTotalComments(stats.getTotalComments());
                report.setTotalVotes(stats.getTotalVotes());
                report.setUpVotes(stats.getUpVotes());
                report.setDownVotes(stats.getDownVotes());

                report.setSlaBreachedCount(stats.getSlaBreached());
                report.setEscalationRate(stats.getEscalationRate());

                report.setGeneratedAt(LocalDateTime.now());

                reportRepository.save(report);

            } catch (Exception e) {
                log.error("Failed to generate report for unit: {}", unit.getId(), e);
                // continue other units instead of failing whole job
            }
        }
    }

    // ✅ safer helper
    private int getPreviousOffset(ReportPeriod period) {
        return switch (period) {
            case DAILY -> 1;
            case WEEKLY -> 7;
            case MONTHLY -> 30;
        };
    }

    private double calculateChange(long current, long previous) {
        if (previous == 0) return current == 0 ? 0 : 100.0;
        return ((double) (current - previous) / previous) * 100;
    }
}