package com.tansen.app.dto.request;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendAccountActivationEmailRequest extends ModelBase {
    private String email;
}
