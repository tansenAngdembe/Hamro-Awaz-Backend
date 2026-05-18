package com.tansen.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadDocumentRequest {
    @NotBlank(message = "Citizenship number is required")
    @Pattern(
            regexp = "^\\d{2}-\\d{2}-\\d{2}-\\d{5}$",
            message = "Citizenship number must be in format 01-03-98-02232"
    )
    private String citizenShipNumber;
    private String nationalIdentityNumber;

    @NotBlank(message = "Province is required")
    private Long provinceUniqueId;

    @NotBlank(message = "District is required")
    private Long districtUniqueId;

}
