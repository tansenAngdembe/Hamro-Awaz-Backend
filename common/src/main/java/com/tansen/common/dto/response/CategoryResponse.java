package com.tansen.common.dto.response;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponse extends ModelBase {
    private String categoryName;
    private String description;
}
