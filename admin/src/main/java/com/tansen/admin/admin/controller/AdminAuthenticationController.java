package com.tansen.admin.admin.controller;

import com.tansen.admin.admin.service.AdminAuthenticationService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.request.AuthenticateUserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = ApiConstant.ADMIN_API)
public class AdminAuthenticationController {

    private final AdminAuthenticationService adminAuthenticationService;

    public AdminAuthenticationController(AdminAuthenticationService adminAuthenticationService) {
        this.adminAuthenticationService = adminAuthenticationService;
    }

    @PostMapping(ApiConstant.LOGIN)
    public ApiResponse<?> authenticate(@RequestBody @Valid AuthenticateUserRequest authenticateUserRequest, HttpServletResponse response){
        return adminAuthenticationService.authenticate(authenticateUserRequest, response);
    }
    @PostMapping(ApiConstant.REFRESH_TOKEN)
    public ApiResponse<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return adminAuthenticationService.refreshToken(request, response);
    }

    @GetMapping(ApiConstant.LOGOUT)
    public ApiResponse<?> logout(HttpServletRequest request, HttpServletResponse response) {
        return adminAuthenticationService.logout(request, response);
    }

    @GetMapping(ApiConstant.CHECK_AUTHENTICATION)
    public ApiResponse<?> checkAuthentication() {
        return adminAuthenticationService.checkAuthentication();}
}
