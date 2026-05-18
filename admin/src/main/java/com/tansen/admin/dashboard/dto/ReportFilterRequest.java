package com.tansen.admin.dashboard.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class ReportFilterRequest extends ModelBase {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate to;

    // Optional: filter by department
    private Long departmentId;

    // For export: "csv" or "pdf"
    private String format = "csv";
}