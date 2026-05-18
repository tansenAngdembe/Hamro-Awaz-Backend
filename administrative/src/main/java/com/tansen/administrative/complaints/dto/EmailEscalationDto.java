package com.tansen.administrative.complaints.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmailEscalationDto {
  private String complaintTitle;
  private String  complaintRule;
  private String category;
  private LocalDateTime createdDate;
  private String priority;
  private String assignedTo;
  private String templateName;
}
