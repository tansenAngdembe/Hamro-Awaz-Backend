package com.tansen.app.category.controller;

import com.tansen.app.category.service.CategoryService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping(ApiConstant.LIST)
    public ApiResponse<?> listCategory( Principal loggedInAdmin){
        return categoryService.listCategory(loggedInAdmin);
    }

}
