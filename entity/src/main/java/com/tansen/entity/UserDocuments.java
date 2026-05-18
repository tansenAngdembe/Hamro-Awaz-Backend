package com.tansen.entity;

import com.tansen.entity.enums.DocumentVerificationStatus;
import com.tansen.entity.enums.RejectionCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_documents")
public class UserDocuments extends AbstractEntity{

    @Column(name = "citizenship_card_front")
    private String citizenshipCardFront;

    @Column(name = "citizenship_card_back")
    private String citizenshipCardBack;

    @Column(name = "national_identity_number")
    private String nationalIdentityNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "municipality_id", nullable = false)
    private AdministrativeUnit municipality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",referencedColumnName = "id",nullable = false, unique = true)
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column( name = "is_document_verified")
    private Boolean isDocumentVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    private DocumentVerificationStatus verificationStatus;

    @Column(name = "unique_id")
    private String uniqueId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rejection_category")
    private RejectionCategory rejectionCategory;

    @Column(name = "rejection_reason", length = 1000)
    private String rejectionReason;

}

