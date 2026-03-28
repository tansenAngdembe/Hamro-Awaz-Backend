package com.tansen.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DocumentVerificationStatus {
    PENDING ("PENDING"),
    APPROVED ("APPROVED"),
    REJECTED ("REJECTED"),
    RESUBMITTED ("RESUBMITTED");
    private String name;

}