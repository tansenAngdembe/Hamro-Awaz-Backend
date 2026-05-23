package com.tansen.app.category.service;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;

import java.security.Principal;

public interface CategoryService {
    ApiResponse<?> listCategory( Principal loggedInAdmin);
}
