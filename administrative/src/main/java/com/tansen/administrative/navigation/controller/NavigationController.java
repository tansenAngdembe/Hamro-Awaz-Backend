package com.tansen.administrative.navigation.controller;

import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.administrative.navigation.service.NavigationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstant.MUNICIPALITY_API)
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
