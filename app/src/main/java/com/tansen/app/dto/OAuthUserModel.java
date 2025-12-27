package com.tansen.app.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OAuthUserModel extends ModelBase {
    private String email;
    private String name;
    private String profileImage;
    private String authProvider;
    private String authProviderId;
}
