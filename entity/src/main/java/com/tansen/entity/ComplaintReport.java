package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "complaint_report")
public class ComplaintReport extends AbstractEntity {

    // Time scope of the report
    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    private LocalDate toDate;

    // Department / administrative filter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrative_id")
    private AdministrativeUnit administrative;

    // ======================
    // OVERVIEW STATS
    // ======================
    @Column(name = "total_complaints")
    private long totalComplaints;

    @Column(name = "resolved")
    private long resolved;

    @Column(name = "pending")
    private long pending;

    @Column(name = "in_progress")
    private long inProgress;

    @Column(name = "escalated")
    private long escalated;

    // ======================
    // MONTH-OVER-MONTH CHANGE (%)
    // ======================
    @Column(name = "total_change_percent")
    private double totalChangePercent;

    @Column(name = "resolved_change_percent")
    private double resolvedChangePercent;

    @Column(name = "in_progress_change_percent")
    private double inProgressChangePercent;

    @Column(name = "escalated_change_percent")
    private double escalatedChangePercent;

    // ======================
    // ENGAGEMENT METRICS
    // ======================

    @Column(name = "total_comments")
    private long totalComments;

    @Column(name = "total_votes")
    private long totalVotes;

    @Column(name = "upvotes")
    private long upVotes;

    @Column(name = "downvotes")
    private long downVotes;

    // ======================
    // PERFORMANCE METRICS
    // ======================



    @Column(name = "sla_breached_count")
    private long slaBreachedCount;

    @Column(name = "escalation_rate")
    private double escalationRate;

    // ======================
    // OPTIONAL BREAKDOWNS
    // ======================

    @Column(name = "top_category_id")
    private long topCategoryId;

    @Column(name = "top_category_count")
    private long topCategoryCount;

    @Column(name = "most_active_user_id")
    private long mostActiveUserId;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

}