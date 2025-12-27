package com.tansen.app.dto.response;

import com.cosmotech.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileResponse extends ModelBase {
    private String profilePictureLink;
}
