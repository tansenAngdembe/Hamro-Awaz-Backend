package com.tansen.admin.admin.dto.response;


import com.tansen.common.dto.ModelBase;
import com.tansen.common.dto.request.AccessGroupDto;
import com.tansen.common.dto.StatusDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListAdminResponse extends ModelBase {
    private String name;
    private String email;
    private String mobileNumber;
    private String uniqueId;
    private AccessGroupDto accessGroup;
    private StatusDto status;
}
