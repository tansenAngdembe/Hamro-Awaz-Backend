package com.tansen.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintUniqueIdDto extends ModelBase{
    private String uniqueId;
    private String remarks;

}
