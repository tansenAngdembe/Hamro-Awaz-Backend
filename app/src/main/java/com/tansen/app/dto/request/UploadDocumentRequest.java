package com.tansen.app.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadDocumentRequest {
    private String nationalIdentityNumber;
    private String municipalityUniqueId;
    private Long provinceUniqueId;
    private Long districtUniqueId;

}
