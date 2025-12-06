package com.tansen.admin.admin.dto.request;

import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.request.AccessGroupDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAdminDetailRequest extends ModelBase {
    @NotBlank(message = "Remarks is required")
    private String remarks;
    @NotBlank(message = "Unique id is required")
    private String uniqueId;
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Mobile Number is required")
    private String mobileNumber;
    @NotBlank(message = "Address is required")
    private String address;
    @NotNull
    @Valid
    private AccessGroupDto accessGroup;
}
