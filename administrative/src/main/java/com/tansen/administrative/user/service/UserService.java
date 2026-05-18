package com.tansen.administrative.user.service;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;

import java.security.Principal;

public interface UserService {
     ApiResponse<?> listAllUsers(SearchParam searchParam, Principal principal);

    }
