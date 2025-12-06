package com.tansen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="provinces")
@AllArgsConstructor
@NoArgsConstructor
public class Province extends AbstractEntity {
    @Column(name="province", nullable = false)
    private String province;
}
