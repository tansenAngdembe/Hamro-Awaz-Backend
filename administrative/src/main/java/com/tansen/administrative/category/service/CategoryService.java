package com.tansen.administrative.category.service;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import com.tansen.administrative.category.dto.CreateCategoryRequest;

import java.security.Principal;

public interface CategoryService {
    ApiResponse<?> listCategory(SearchParam searchParam, Principal loggedInAdmin);
    ApiResponse<?> createCategory(CreateCategoryRequest request, Principal loggedInAdmin);
}
