package com.tansen.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Priority {
    HIGH ("HIGH"),
    MEDIUM  ("MEDIUM"),
    LOW ("LOW");
    private String name;
}
