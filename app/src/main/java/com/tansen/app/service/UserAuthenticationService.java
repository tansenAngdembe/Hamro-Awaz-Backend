package com.tansen.app.service;

import com.tansen.app.dto.request.OauthExchangeRequest;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.request.AuthenticateUserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserAuthenticationService {
    ApiResponse<?> authenticate(AuthenticateUserRequest authenticateUserRequest);
    ApiResponse<?> refreshToken(HttpServletRequest request, HttpServletResponse response);
    ApiResponse<?> logout(String authHeader, HttpServletResponse response);
    ApiResponse<?> checkAuthentication(HttpServletRequest request);
}
