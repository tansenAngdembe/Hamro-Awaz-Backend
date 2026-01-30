package com.tansen.entity;

import com.tansen.entity.enums.Priority;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "complaints")
public class Complaint  extends  AbstractEntity{
    @Column(name = "unique_id", nullable = false)
    private String uniqueId;

    @Column(name = "complaint_title", nullable = false)
    private String complaintTitle;

    @Column(name = "complaint_description", nullable = false, columnDefinition = "TEXT")
    private String complaintDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private ComplaintStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by", nullable = false)
    private User reportedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private AuthorityUser assignedTo;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "active")
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    @Column(name = "photoUrl",nullable = false)
    private String photoUrl;

    @Column(name = "created_date",nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @OneToMany(
            mappedBy = "complaint",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Vote> votes = new ArrayList<>();


}
