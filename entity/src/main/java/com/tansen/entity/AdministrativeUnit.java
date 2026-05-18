package com.tansen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "municipality")
public class AdministrativeUnit extends AbstractEntity{
    @Column(name = "govrenment_name", nullable = false)
    private String governmentName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name ="code", nullable = false, unique = true)
    private String code;

    @ManyToOne(optional = false)
    @JoinColumn(name="status_id", referencedColumnName = "id")
    private Status status;

    @Column(name = "document_url", nullable = false)
    private String documentUrl;

    @Column(name = "unique_id", nullable = false)
    private String uniqueId;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name="province_id", referencedColumnName = "id")
    private Province province;

    @ManyToOne(optional = false)
    @JoinColumn(name="district_id", referencedColumnName = "id")
    private District district;

    @ManyToOne(optional = false)
    @JoinColumn(name="local_level_id", referencedColumnName = "id")
    private LocalLevel localLevel;

    @Column(name = "latitude", nullable = false)
    private String latitude;

    @Column(name = "longitude", nullable = false)
    private String longitude;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "municipality", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "municipality", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Escalation> escalations = new ArrayList<>();

}
