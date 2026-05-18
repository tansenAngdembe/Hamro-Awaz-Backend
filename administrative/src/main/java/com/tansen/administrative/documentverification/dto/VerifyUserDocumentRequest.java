package com.tansen.administrative.documentverification.dto;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserDocumentRequest extends ModelBase {
    @NotBlank(message = "Unique ID is required")
    private String uniqueId;
}
