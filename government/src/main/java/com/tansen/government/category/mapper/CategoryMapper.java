package com.tansen.government.category.mapper;

import com.tansen.common.constant.ComplaintStatusConstant;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.Category;
import com.tansen.entity.Complaint;
import com.tansen.entity.Municipality;
import com.tansen.government.category.dto.CreateCategoryRequest;
import com.tansen.government.category.dto.ListCategoryResponse;
import com.tansen.government.complaints.dto.response.ListComplainsResponse;
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
    public Category createCategory(CreateCategoryRequest request, Municipality municipality) {
        Category category = new Category();
        category.setCategoryName(request.getCategoryName());
        category.setDescription(request.getDescription());
        category.setUniqueId(UUID.randomUUID().toString());
        category.setMunicipality(municipality);
        category.setCreatedAt(LocalDateTime.now());
        return category;
    }
}
