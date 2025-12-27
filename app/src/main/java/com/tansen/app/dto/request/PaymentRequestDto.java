package com.tansen.app.dto.request;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaymentRequestDto extends ModelBase {
    private List<String> bookedUniqueIds;
}
