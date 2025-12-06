package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name="email_templates")
public class EmailTemplate extends AbstractEntity {
    @Column(name="name", nullable = false)
    private String name;

    @Column(name="content", nullable = false)
    private String template;

    @Column(name="created_date")
    private LocalDateTime createdDate;
}
