package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "complaint_coordinates")
public class ComplaintCoordinates extends AbstractEntity {

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private Double latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private Double longitude;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "complaint_id",
            nullable = false,
            unique = true
    )
    private Complaint complaint;
}
