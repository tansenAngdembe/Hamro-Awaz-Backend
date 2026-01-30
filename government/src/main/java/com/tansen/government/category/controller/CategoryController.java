package com.tansen.government.category.controller;

import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.category.dto.CreateCategoryRequest;
import com.tansen.government.category.service.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.MUNICIPALITY_API + ApiConstant.SLASH + ApiConstant.CATEGORY)
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(ApiConstant.CREATE)
    @PreAuthorize("hasAuthority('STAFF')")
    public ApiResponse<?> createCategory(@RequestBody CreateCategoryRequest request, Principal loggedInAdmin) {
        return categoryService.createCategory( request,loggedInAdmin);
    }
    @PostMapping(ApiConstant.LIST)
    @PreAuthorize("hasAuthority('STAFF')")
    public ApiResponse<?> listCategory(SearchParam searchParam, Principal loggedInAdmin){
        return categoryService.listCategory(searchParam,loggedInAdmin);
    }

}
