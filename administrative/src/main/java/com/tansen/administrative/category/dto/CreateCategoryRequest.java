package com.tansen.administrative.category.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequest extends ModelBase {
    private String categoryName;
    private String description;

}
