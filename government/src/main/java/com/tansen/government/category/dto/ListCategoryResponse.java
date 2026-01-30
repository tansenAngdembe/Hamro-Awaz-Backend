package com.tansen.government.category.dto;

import com.tansen.common.dto.ModelBase;
import com.tansen.government.complaints.dto.response.ListComplainsResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ListCategoryResponse extends ModelBase {
    private String categoryName;
    private String description;
    private String uniqueId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
