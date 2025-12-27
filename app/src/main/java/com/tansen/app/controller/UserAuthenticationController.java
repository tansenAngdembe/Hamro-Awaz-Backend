package com.tansen.app.controller;


import com.tansen.app.service.UserAuthenticationService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.request.AuthenticateUserRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = ApiConstant.API)
@RestController
public class UserAuthenticationController {
    private final UserAuthenticationService userAuthenticationService;

    public UserAuthenticationController(UserAuthenticationService userAuthenticationService) {
        this.userAuthenticationService = userAuthenticationService;
    }

    @PostMapping(ApiConstant.LOGIN)
    public ApiResponse<?> authenticate(@RequestBody @Valid AuthenticateUserRequest authenticateUserRequest) {
        return userAuthenticationService.authenticate(authenticateUserRequest);
    }
    @PostMapping(ApiConstant.REFRESH_TOKEN)
    public ApiResponse<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return userAuthenticationService.refreshToken(request, response);
    }

    @PostMapping(ApiConstant.LOGOUT)
    public ApiResponse<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                 HttpServletResponse response) {
        return userAuthenticationService.logout(authHeader, response);
    }

    @GetMapping(ApiConstant.CHECK_AUTHENTICATION)
    public ApiResponse<?> checkAuthentication(HttpServletRequest request) {
        return userAuthenticationService.checkAuthentication(request);
    }

}

