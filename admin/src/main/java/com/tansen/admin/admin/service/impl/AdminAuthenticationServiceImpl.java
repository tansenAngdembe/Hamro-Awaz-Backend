package com.tansen.admin.admin.service.impl;


import com.tansen.admin.admin.service.AdminAuthenticationService;
import com.tansen.admin.security.JwtService;
import com.tansen.admin.util.AdminTokenUtil;
import com.tansen.common.constant.StatusConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.common.dto.request.AuthenticateUserRequest;
import com.tansen.entity.Admin;
import com.tansen.entity.AdminToken;
import com.tansen.repository.AdminRepository;
import com.tansen.repository.AdminTokenRepository;
import com.tansen.repository.StatusRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AdminAuthenticationServiceImpl implements AdminAuthenticationService {
    private static final Logger LOG = LoggerFactory.getLogger(AdminAuthenticationServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final AdminRepository adminRepository;
    private final StatusRepository statusRepository;
    private final JwtService jwtService;
    private final AdminTokenRepository adminTokenRepository;

    public AdminAuthenticationServiceImpl(AuthenticationManager authenticationManager, AdminRepository adminRepository, StatusRepository statusRepository, JwtService jwtService, AdminTokenRepository adminTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.adminRepository = adminRepository;
        this.statusRepository = statusRepository;
        this.jwtService = jwtService;
        this.adminTokenRepository = adminTokenRepository;
    }

    @Override
    @Transactional
    public ApiResponse<?> authenticate(AuthenticateUserRequest authenticateUserRequest, HttpServletResponse response) {
        Admin admin = adminRepository.findByEmail(authenticateUserRequest.getEmail());
        if (admin == null || admin.getStatus().equals(statusRepository.findByName(StatusConstant.DELETED.getName()))) {
            return ResponseUtil.getFailureResponse("The account doesn't exist.");
        }
        if (admin.getStatus().equals(statusRepository.findByName(StatusConstant.BLOCKED.getName()))) {
            return ResponseUtil.getFailureResponse("The user is currently blocked. Please contact support.");
        }
        if (admin.getStatus().equals(statusRepository.findByName(StatusConstant.ACTIVE.getName()))) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authenticateUserRequest.getEmail(), authenticateUserRequest.getPassword()));
                authentication.isAuthenticated();
                if (authentication.isAuthenticated()) {
                    AdminToken adminToken = AdminTokenUtil.saveToken(admin,
                            jwtService.generateAccessToken(admin),
                            adminTokenRepository,
                            jwtService.generateRefreshToken(admin));
                    jwtService.setHttpOnlyCookie(response, "accessToken", adminToken.getAccessToken(), 60 * 60 * 24);
                    jwtService.setHttpOnlyCookie(response, "refreshToken", adminToken.getRefreshToken(), 60 * 60 * 24);
                    adminRepository.updateLastLoggedInTime(authenticateUserRequest.getEmail(), LocalDateTime.now());
                    adminRepository.updateWrongPasswordAttemptCount(authenticateUserRequest.getEmail(), 0);
                    LOG.info("Admin logged in successfully - {}", authenticateUserRequest.getEmail());
                    return ResponseUtil.getSuccessfulApiResponse("You have successfully logged in.");
                }
            } catch (BadCredentialsException e) {
                adminRepository.updateWrongPasswordAttemptCount(admin.getEmail(), admin.getWrongPasswordAttemptCount() + 1);
                return ResponseUtil.getFailureResponse("Incorrect password.");
            }
        }
        return ResponseUtil.getFailureResponse("Please verify to continue.");
    }
    @Override
    public ApiResponse<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null) {
            return ResponseUtil.getFailureResponse("No refresh token found. Please log in again.");
        }
        Admin admin = adminRepository.findByEmail(jwtService.extractEmail(refreshToken));
        if (admin == null) {
            return ResponseUtil.getFailureResponse("The refresh token is invalid. Please log in again to continue.");
        }
        boolean isRefreshTokenValid = jwtService.validateRefreshToken(refreshToken, admin);
        if (isRefreshTokenValid) {
            AdminToken token = AdminTokenUtil.saveToken(admin,
                    jwtService.generateAccessToken(admin),
                    adminTokenRepository,
                    jwtService.generateRefreshToken(admin));
            jwtService.setHttpOnlyCookie(response,"accessToken",token.getAccessToken(),60*60*24);
            jwtService.setHttpOnlyCookie(response,"refreshToken",token.getRefreshToken(),60*60*24);
            AdminTokenUtil.invalidateToken(refreshToken, adminTokenRepository::findByRefreshToken, adminTokenRepository);
            return ResponseUtil.getSuccessfulApiResponse("Tokens refreshed successfully.");
        }
        return ResponseUtil.getFailureResponse("The refresh token is invalid. Please log in again to continue.");
    }


    @Override
    public ApiResponse<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }
        AdminToken storedToken = adminTokenRepository.findByAccessTokenAndLoggedOutFalse(accessToken).orElse(null);
        if (storedToken != null) {
            storedToken.setLoggedOut(true);
            adminTokenRepository.save(storedToken);
            jwtService.clearCookie("accessToken", response);
            jwtService.clearCookie("refreshToken", response);
            SecurityContextHolder.clearContext();
            return ResponseUtil.getSuccessfulApiResponse("You have been successfully logged out.");
        }
        return ResponseUtil.getFailureResponse("Logout failed. An error occurred.");
    }

    @Override
    public ApiResponse<?> checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return ResponseUtil.getAuthenticatedApiResponse("AUTHENTICATED");
        }
        return ResponseUtil.getUnAuthorized("UNAUTHENTICATED");
    }
}
