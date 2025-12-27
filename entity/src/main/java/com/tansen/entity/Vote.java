package com.tansen.entity;

import com.tansen.entity.enums.VoteType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type", nullable = false)
    private VoteType voteType;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "voted_by", nullable = false)
    private User votedBy;

    @Column(name = "remarks")
    private String remarks;

}
