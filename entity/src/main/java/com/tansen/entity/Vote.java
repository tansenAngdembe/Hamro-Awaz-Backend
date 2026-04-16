package com.tansen.entity;

import com.tansen.entity.enums.VoteType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table( name = "votes",
        uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"complaint_id", "voted_by"}
        )
        }
)
public class Vote extends AbstractEntity{
    @Column(name = "unique_id")
    private String uniqueId;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "voted_by", nullable = false)
    private User user;

    @Column(name = "remarks")
    private String remarks;


    @Column(name = "voted_at", nullable = false)
    private LocalDateTime votedAt;

    @Column(name = "synced_from_redis")
    private Boolean syncedFromRedis = false;

}
