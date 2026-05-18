package com.tansen.admin.dashboard.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReportSummaryResponse extends ModelBase {

    // Overview stat cards
    private long totalComplaints;
    private long resolved;
    private long pending;
    private long inProgress;
    private long escalated;

    // Month-over-month delta (percentage change vs previous month)
    private double totalChangePercent;
    private double resolvedChangePercent;
    private double inProgressChangePercent;
    private double escalatedChangePercent;
}

