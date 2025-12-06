package com.tansen.admin.navigation.controller;


import com.tansen.admin.navigation.service.NavigationService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstant.ADMIN_API)
public class NavigationController {
    private final NavigationService navigationService;

    public NavigationController(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    @GetMapping(ApiConstant.NAVIGATION)
    public ApiResponse<?> getAllNavigation() {
        return navigationService.getAllNavigation();
    }
}
