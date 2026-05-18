package com.tansen.administrative.category.mapper;

import com.tansen.entity.AdministrativeUnit;
import com.tansen.entity.Category;
import com.tansen.administrative.category.dto.CreateCategoryRequest;
import com.tansen.administrative.category.dto.ListCategoryResponse;
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
    public Category createCategory(CreateCategoryRequest request, AdministrativeUnit municipality) {
        Category category = new Category();
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setUniqueId(UUID.randomUUID().toString());
        category.setMunicipality(municipality);
        category.setCreatedAt(LocalDateTime.now());
        return category;
    }
}
