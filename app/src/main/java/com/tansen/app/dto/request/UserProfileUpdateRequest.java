package com.tansen.app.dto.request;

import com.tansen.common.dto.ModelBase;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateRequest  extends ModelBase {
    @NotNull(message = "FUllName is required")
    private String fullName;
    @NotNull(message = "MobileNumber is Required")
    private String mobileNumber;
}
