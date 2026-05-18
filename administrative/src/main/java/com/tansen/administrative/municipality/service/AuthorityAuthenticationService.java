package com.tansen.administrative.municipality.service;

import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.request.AuthenticateUserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthorityAuthenticationService {
    ApiResponse<?> authenticate(AuthenticateUserRequest authenticateUserRequest, HttpServletResponse response);
    ApiResponse<?> refreshToken(HttpServletRequest request, HttpServletResponse response);
    ApiResponse<?> logout(HttpServletRequest request, HttpServletResponse response);
    ApiResponse<?> checkAuthentication();
}
