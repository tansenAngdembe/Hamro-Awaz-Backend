package com.tansen.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusConstant {
    ACTIVE("ACTIVE", "ACTIVE"),
    DELETED("DELETED", "DELETED"),
    PENDING("PENDING", "PENDING"),
    BLOCKED("BLOCKED", "BLOCKED");

    private final String name;
    private final String description;
}
