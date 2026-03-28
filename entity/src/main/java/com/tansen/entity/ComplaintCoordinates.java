package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@Entity
@Table(name = "complaint_coordinates")
public class ComplaintCoordinates extends AbstractEntity {

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "complaint_id",
            nullable = false,
            unique = true
    )
    private Complaint complaint;
}
