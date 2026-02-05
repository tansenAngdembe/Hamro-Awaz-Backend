package com.tansen.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Priority {
    HIGH ("HIGH"),
    MEDIUM  ("MEDIUM"),
    LOW ("LOW"),
    ESCALATED("ESCALATED");
    private String name;
}
