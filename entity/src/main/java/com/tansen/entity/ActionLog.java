package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="action_logs")
public class ActionLog extends AbstractEntity {
    @Column(name="remarks", nullable = false)
    private String remarks;

    @Column(name="target_type", nullable = false)
    private String targetType;

    @Column(name="target_id", nullable = false)
    private Long targetId;

    @Column(name="action_type", nullable = false)
    private String actionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="action_by", nullable=false, referencedColumnName = "id")
    private Admin actionBy;

    @Column(name="ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "action_date")
    private LocalDateTime actionDate;

}
