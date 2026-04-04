package com.tansen.admin.user.service;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;

public interface UserService {
    ApiResponse<?> listAllUsers(SearchParam searchParam);

}
