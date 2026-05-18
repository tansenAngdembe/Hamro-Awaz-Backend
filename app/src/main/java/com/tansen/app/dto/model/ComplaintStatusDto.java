package com.tansen.app.dto.model;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ComplaintStatusDto  extends ModelBase {
    private String name;
    private String description;
}
