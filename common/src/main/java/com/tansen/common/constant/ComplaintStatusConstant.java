package com.tansen.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ComplaintStatusConstant {
    NEW("NEW", "New complaint registered"),
    IN_REVIEW("IN_REVIEW", "Complaint is under review"),
    ASSIGNED("ASSIGNED", "Complaint assigned to authority"),
    IN_PROGRESS("IN_PROGRESS", "Work in progress"),
    RESOLVED("RESOLVED", "Issue successfully resolved"),
    ESCALATED("ESCALATED", "Complaint escalated to higher authority"),
    REJECTED("REJECTED", "Complaint rejected as invalid"),
    CLOSED("CLOSED","Complaint closed");

    private final String name;
    private final String description;
}
