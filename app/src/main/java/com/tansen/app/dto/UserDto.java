package com.tansen.app.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends ModelBase {
    private String fullName;
    private String phoneNumber;
    private String uniqueId;
    private String profilePictureLink;
}
