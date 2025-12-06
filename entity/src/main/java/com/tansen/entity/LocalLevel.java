package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="local_levels")
public class LocalLevel extends AbstractEntity{
    @Column(name="local_level", nullable = true)
    private String localLevel;

    @Column(name="local_level_code", nullable = false)
    private Integer localLevelCode;

    @Column(name="total_wards")
    private Integer totalWards;

    @JoinColumn(name="district", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private District district;
}
