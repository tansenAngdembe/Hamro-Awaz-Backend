package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "escalations")
public class Escalation extends AbstractEntity {

    @Column(name = "max_resolution_time", nullable = false)
    private int maxResolutionHours;

    @Column( name = "escalation_time", nullable = false)
    private int escalationTime;

    @Column(name = "response_time")
    private int responseTime;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "rule_name", nullable = false)
    private String ruleName;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "municipality_id", nullable = false)
    private AdministrativeUnit municipality;

}
