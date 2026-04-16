package com.tansen.government.user.controller;

import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import com.tansen.government.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping(ApiConstant.MUNICIPALITY_API + ApiConstant.SLASH + ApiConstant.CITIZEN)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(ApiConstant.LIST)
    public ApiResponse<?> listAllUsers(SearchParam searchParam, Principal loggedInUser) {
        return userService.listAllUsers(searchParam, loggedInUser);
    }
}
