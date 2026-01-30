package com.tansen.government.municipality.controller;

import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.request.AuthenticateUserRequest;
import com.tansen.government.municipality.service.AuthorityAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstant.MUNICIPALITY_API)
public class AuthorityAuthenticationController {
    private final AuthorityAuthenticationService vendorAuthenticationService;

    public AuthorityAuthenticationController(AuthorityAuthenticationService vendorAuthenticationService) {
        this.vendorAuthenticationService = vendorAuthenticationService;
    }

    @PostMapping(ApiConstant.LOGIN)
    public ApiResponse<?> authenticate(@RequestBody @Valid AuthenticateUserRequest authenticateUserRequest, HttpServletResponse response) {
        return vendorAuthenticationService.authenticate(authenticateUserRequest, response);
    }

    @PostMapping(ApiConstant.REFRESH_TOKEN)
    public ApiResponse<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return vendorAuthenticationService.refreshToken(request, response);
    }
    @PostMapping(ApiConstant.LOGOUT)
    public ApiResponse<?> logout(HttpServletRequest request, HttpServletResponse response) {
        return vendorAuthenticationService.logout(request, response);
    }
    @PostMapping(ApiConstant.CHECK_AUTHENTICATION)
    public ApiResponse<?> checkAuthentication() {
        return vendorAuthenticationService.checkAuthentication();
    }
}
