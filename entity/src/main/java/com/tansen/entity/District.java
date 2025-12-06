package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="districts")
public class District  extends AbstractEntity {
    @Column(name="district_name", nullable = false)
    private String districtName;

    @JoinColumn(name="province_id", nullable = true)
    @ManyToOne(optional = false)
    private Province province;
}
