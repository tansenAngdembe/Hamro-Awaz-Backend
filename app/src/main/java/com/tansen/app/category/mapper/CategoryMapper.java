package com.tansen.app.category.mapper;

import com.tansen.app.category.dto.ListCategoryResponse;
import com.tansen.entity.AdministrativeUnit;
import com.tansen.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class CategoryMapper {


    public abstract ListCategoryResponse entityToResponse(Category complaint);
    public List<ListCategoryResponse> listCategoryResponses(List<Category> actionLog) {
        return actionLog.stream().map(this::entityToResponse).collect(Collectors.toList());
    }

}
