package com.tansen.app.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateComplaintRequest extends ModelBase {
    @NotBlank(message = "Complaint uniqueId")
    private String complaintUniqueId;
    @NotBlank(message = "Complaint title shouldn't be blank.")
    private String complaintTitle;
    @NotBlank(message = "Complaint description shouldn't be blank.")
    @Size(min = 20, max = 500, message = "Complaint description should at-least  20 characters.")
    private String complaintDescription;
    @NotBlank(message = "District ID is required")
    private String municipality;
    @NotBlank(message = "At-least one should be available.")
    private String photoUrl;
    @NotBlank(message = "Category shouldn't be blank")
    private String categoryId;
}
