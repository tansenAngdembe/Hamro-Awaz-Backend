package com.tansen.government.category.service;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ComplaintUniqueIdDto;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.category.dto.CreateCategoryRequest;
import com.tansen.government.complaints.dto.request.ComplaintAssignRequest;

import java.security.Principal;

public interface CategoryService {
    ApiResponse<?> listCategory(SearchParam searchParam, Principal loggedInAdmin);
    ApiResponse<?> createCategory(CreateCategoryRequest request, Principal loggedInAdmin);
}
