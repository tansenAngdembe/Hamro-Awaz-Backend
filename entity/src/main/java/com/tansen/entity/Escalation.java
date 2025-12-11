package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "escalations")
public class Escalation extends AbstractEntity {

    @Column(name = "max_resolution_hours", nullable = false)
    private int maxResolutionHours;

    @Column( name = "escalation_level", nullable = false)
    private int escalationLevel;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "municipality_id", nullable = false)
    private Municipality municipality;

}
