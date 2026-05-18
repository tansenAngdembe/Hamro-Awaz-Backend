package com.tansen.administrative.municipality.service.impl;

import com.tansen.common.constant.StatusConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.common.dto.request.AuthenticateUserRequest;
import com.tansen.entity.AuthorityUser;
import com.tansen.entity.AuthorityUserToken;
import com.tansen.administrative.core.security.JwtService;
import com.tansen.administrative.core.util.AuthorityTokenUtil;
import com.tansen.administrative.municipality.service.AuthorityAuthenticationService;
import com.tansen.repository.AuthorityUserRepository;
import com.tansen.repository.AuthorityUserTokenRepository;
import com.tansen.repository.StatusRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthorityAuthenticationServiceImpl implements AuthorityAuthenticationService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorityAuthenticationServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final StatusRepository statusRepository;
    private final AuthorityUserTokenRepository authorityUserTokenRepository;
    private final AuthorityUserRepository authorityUserRepository;

    public AuthorityAuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService, StatusRepository statusRepository, AuthorityUserTokenRepository vendorUserTokenRepository, AuthorityUserRepository vendorUserRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.statusRepository = statusRepository;
        this.authorityUserTokenRepository = vendorUserTokenRepository;
        this.authorityUserRepository = vendorUserRepository;
    }

    @Override
    @Transactional
    public ApiResponse<?> authenticate(AuthenticateUserRequest authenticateUserRequest, HttpServletResponse response) {
        Optional<AuthorityUser> vendorUsersOptional = authorityUserRepository.findByEmail(authenticateUserRequest.getEmail());
        if (vendorUsersOptional.isEmpty()){
            LOG.error("User not found with email {}", authenticateUserRequest.getEmail());
            return ResponseUtil.getFailureResponse("User not found with email " + authenticateUserRequest.getEmail());
        }

        AuthorityUser vendorUser = vendorUsersOptional.get();
        if (vendorUser.getStatus().equals(statusRepository.findByName(StatusConstant.DELETED.getName()))) {
            return ResponseUtil.getFailureResponse("The account doesn't exist.");
        }
        if (vendorUser.getStatus().equals(statusRepository.findByName(StatusConstant.BLOCKED.getName()))) {
            return ResponseUtil.getFailureResponse("The user is currently blocked. Please contact support.");
        }
        if (vendorUser.getStatus().equals(statusRepository.findByName(StatusConstant.ACTIVE.getName()))) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authenticateUserRequest.getEmail(), authenticateUserRequest.getPassword()));
                authentication.isAuthenticated();
                if (authentication.isAuthenticated()) {
                    AuthorityUserToken vendorUserToken = AuthorityTokenUtil.saveToken(vendorUser,
                            jwtService.generateAccessToken(vendorUser),
                            authorityUserTokenRepository,
                            jwtService.generateRefreshToken(vendorUser));
                    jwtService.setHttpOnlyCookie(response, "accessToken", vendorUserToken.getAccessToken(), 60 * 60 * 24);
                    jwtService.setHttpOnlyCookie(response, "refreshToken", vendorUserToken.getRefreshToken(), 60 * 60 * 24);
                    authorityUserRepository.updateLastLoggedInTime(authenticateUserRequest.getEmail(), LocalDateTime.now());
                    authorityUserRepository.updateWrongOtpAuthAttemptCount(authenticateUserRequest.getEmail(), 0);
                    LOG.info("Vendor logged in successfully - {}", authenticateUserRequest.getEmail());
                    return ResponseUtil.getSuccessfulApiResponse("You have successfully logged in.");
                }
            } catch (BadCredentialsException e) {
                int currentCount = vendorUser.getWrongPasswordAttemptCount() != null
                        ? vendorUser.getWrongPasswordAttemptCount()
                        : 0;
                authorityUserRepository.updateWrongOtpAuthAttemptCount(
                        vendorUser.getEmail(), currentCount + 1
                );
                return ResponseUtil.getFailureResponse("The password you entered is incorrect.");
            }
        }
        return ResponseUtil.getFailureResponse("This account is awaiting verification. Please verify to continue.");
    }

    @Override
    public ApiResponse<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = null;
        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("refreshToken")) {
                    refreshToken =  cookie.getValue();
                    break;
                }
            }
        }
        if (refreshToken == null || refreshToken.isEmpty()) {
                return ResponseUtil.getFailureResponse("No refresh token token found. Please log in again to continue.");
        }
        Optional<AuthorityUser> vendorUserOptional = authorityUserRepository.findByEmail(jwtService.extractEmail(refreshToken));
        if (vendorUserOptional.isEmpty()) {
            return ResponseUtil.getFailureResponse("The refresh token is invalid. Please log in again to continue.");
        }
        AuthorityUser vendorUser = vendorUserOptional.get();
        boolean isRefreshTokenValid = jwtService.validateRefreshToken(refreshToken,vendorUser);
        if (isRefreshTokenValid) {
            AuthorityUserToken token = AuthorityTokenUtil.saveToken(
                    vendorUser,
                    jwtService.generateAccessToken(vendorUser),
                    authorityUserTokenRepository,
                    jwtService.generateRefreshToken(vendorUser));
            jwtService.setHttpOnlyCookie(response, "accessToken", token.getAccessToken(), 60 * 60 *24);
            jwtService.setHttpOnlyCookie(response, "refreshToken", token.getRefreshToken(), 60 * 60 *24 * 2);
            AuthorityTokenUtil.invalidateToken(refreshToken, authorityUserTokenRepository::findByRefreshToken, authorityUserTokenRepository);
            return ResponseUtil.getSuccessfulApiResponse("Token refreshed successfully.");

        }
        return ResponseUtil.getSuccessfulApiResponse("The refresh token is invalid. Please log in again to continue.");
    }

    @Override
    public ApiResponse<?> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = null;
        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if(cookie.getName().equals("accessToken")) {
                    accessToken = cookie.getValue();
                    break;
                }
            }
        }
        AuthorityUserToken  storeToken = authorityUserTokenRepository.findByAccessTokenAndLoggedOutFalse(accessToken).orElse(null);
        if (storeToken != null) {
            storeToken.setLoggedOut(true);
            authorityUserTokenRepository.save(storeToken);
            jwtService.clearCookie("accessToken",response);
            jwtService.clearCookie("refreshToken",response);
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
