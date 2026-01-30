package com.tansen.app.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintUniqueIdRequest extends ModelBase {
    @NotBlank(message = "Complaint uniqueId")
    private String complaintUniqueId;
}
