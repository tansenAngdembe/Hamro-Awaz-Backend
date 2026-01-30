package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "authority_user_action_log")
public class AuthorityUserActionLog  extends AbstractEntity{
    @Column(name = "remarks")
    private String remarks;

    @Column(name="target_type", nullable = false)
    private String targetType;

    @Column(name="target_id", nullable = false)
    private Long targetId;

    @Column(name="action_type", nullable = false)
    private String actionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="action_by", nullable=false, referencedColumnName = "id")
    private AuthorityUser actionBy;

    @Column(name="ip_address", nullable = false)
    private String ipAddress;

    @Column(name = "action_date")
    private LocalDateTime actionDate;

}
