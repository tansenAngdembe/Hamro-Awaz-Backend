package com.tansen.common.dto.model;

import com.tansen.common.dto.ModelBase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest extends ModelBase {
    private String recipient;
    private String subject;
    private String message;
}
