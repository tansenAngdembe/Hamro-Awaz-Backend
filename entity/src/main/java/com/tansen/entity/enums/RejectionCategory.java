package com.tansen.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RejectionCategory {
    BLURRY_IMAGE ("BLURRY_IMAGE"),
    INVALID_ID_NUMBER ("INVALID_ID_NUMBER"),
    DOCUMENT_EXPIRED ("DOCUMENT_EXPIRED"),
    MISMATCHED_INFORMATION ("MISMATCHED_INFORMATION"),
    INCOMPLETE_UPLOAD ("INCOMPLETE_UPLOAD"),;
    private String name;
}
