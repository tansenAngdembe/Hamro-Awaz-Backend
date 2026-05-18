package com.tansen.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DocumentVerificationStatus {
    PENDING ("PENDING"),
    APPROVED ("APPROVED"),
    REJECTED ("REJECTED"),
    RESUBMITTED ("RESUBMITTED"),
    VERIFIED ("VERIFIED");
    private String name;

}