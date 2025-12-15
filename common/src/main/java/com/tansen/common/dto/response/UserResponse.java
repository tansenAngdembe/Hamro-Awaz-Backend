package com.tansen.common.dto.response;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse extends ModelBase {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String profilePictureLink;

}
