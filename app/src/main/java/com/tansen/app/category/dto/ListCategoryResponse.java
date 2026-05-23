package com.tansen.app.category.dto;

import com.tansen.common.dto.ModelBase;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ListCategoryResponse extends ModelBase {
    private String categoryName;
    private String uniqueId;


}
