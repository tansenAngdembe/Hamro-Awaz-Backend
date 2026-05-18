package com.tansen.admin.user.controller;

import com.tansen.admin.admin.dto.request.CreateAdminRequest;
import com.tansen.admin.admin.service.AdminService;
import com.tansen.admin.user.service.UserService;
import com.tansen.common.constant.ApiConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.SearchParam;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
@RestController
@RequestMapping(ApiConstant.ADMIN_API)
public class UserController {


        private final UserService userService;

        public UserController(UserService userService) {
            this.userService = userService;
        }

        @PostMapping(ApiConstant.CITIZEN + ApiConstant.SLASH + ApiConstant.LIST)
        @PreAuthorize("hasAuthority('USERS')")
        public ApiResponse<?> listAllUsers(SearchParam searchParam){
            return userService.listAllUsers(searchParam);
        }

    }
