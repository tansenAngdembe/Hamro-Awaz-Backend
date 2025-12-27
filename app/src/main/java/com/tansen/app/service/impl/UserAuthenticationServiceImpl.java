package com.tansen.app.service.impl;


import com.tansen.app.core.model.UserPrincipal;
import com.tansen.app.core.security.JwtService;
import com.tansen.app.core.util.UserTokenUtil;
import com.tansen.app.dto.request.OauthExchangeRequest;
import com.tansen.app.service.UserAuthenticationService;
import com.tansen.common.constant.StatusConstant;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.common.dto.request.AuthenticateUserRequest;
import com.tansen.common.dto.response.UserAuthenticationResponse;
import com.tansen.entity.User;
import com.tansen.entity.UserToken;
import com.tansen.repository.StatusRepository;
import com.tansen.repository.UserRepository;
import com.tansen.repository.UserTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    private static final Logger LOG = LoggerFactory.getLogger(UserAuthenticationServiceImpl.class);
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final StatusRepository statusRepository;
    private final UserTokenRepository userTokenRepository;

    public UserAuthenticationServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, StatusRepository statusRepository, UserTokenRepository userTokenRepository ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.statusRepository = statusRepository;
        this.userTokenRepository = userTokenRepository;
    }

    @Override
    @Transactional
    public ApiResponse<?> authenticate(AuthenticateUserRequest authenticateUserRequest) {

        User user = userRepository.findByEmailOrPhoneNumber(authenticateUserRequest.getEmail(), authenticateUserRequest.getEmail());
        if (user == null || user.getStatus().equals(statusRepository.findByName(StatusConstant.DELETED.getName()))) {
            return ResponseUtil.getFailureResponse("The account doesn't exist.");
        }
        if (user.getStatus().equals(statusRepository.findByName(StatusConstant.BLOCKED.getName()))) {
            return ResponseUtil.getFailureResponse("The user is currently blocked. Please contact support.");
        }
        if (user.getStatus().equals(statusRepository.findByName(StatusConstant.ACTIVE.getName()))) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getEmail(), authenticateUserRequest.getPassword()));
                authentication.isAuthenticated();
                if (authentication.isAuthenticated()) {
                    UserToken userToken = UserTokenUtil.saveToken(user,
                            jwtService.generateAccessToken(user),
                            userTokenRepository,
                            jwtService.generateRefreshToken(user));
                    UserAuthenticationResponse userAuthenticationResponse = new UserAuthenticationResponse();
                    userAuthenticationResponse.setAccessToken(userToken.getAccessToken());
                    userAuthenticationResponse.setRefreshToken(userToken.getRefreshToken());
                    userRepository.updateLastLoggedInTime(authenticateUserRequest.getEmail(), new Date());
                    userRepository.updateWrongPasswordAttemptCount(authenticateUserRequest.getEmail(), 0);




                    return ResponseUtil.getSuccessfulApiResponse(userAuthenticationResponse, "You have successfully logged in.");
                }
            } catch (BadCredentialsException e) {
                userRepository.updateWrongPasswordAttemptCount(user.getEmail(), user.getWrongPasswordAttemptCount() + 1);
                return ResponseUtil.getFailureResponse("The password you entered is incorrect.");
            }
        }
        return ResponseUtil.getFailureResponse("This account is awaiting verification. Please verify to continue.", 403);
    }

    @Override
    public ApiResponse<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseUtil.getFailureResponse("The authentication token is invalid. Please log in again to continue.");
        }

        String refreshToken = authHeader.substring(7);
        User user = userRepository.findByEmail(jwtService.extractEmail(refreshToken));
        if (user == null) {
            return ResponseUtil.getFailureResponse("The refresh token is invalid. Please log in again to continue.");
        }
        UserPrincipal userPrincipal = new UserPrincipal(user);
        boolean isRefreshTokenValid = jwtService.validateRefreshToken(refreshToken, userPrincipal);
        if (isRefreshTokenValid) {
            UserToken token = UserTokenUtil.saveToken(user,
                    jwtService.generateAccessToken(user),
                    userTokenRepository,
                    jwtService.generateRefreshToken(user));
            UserTokenUtil.invalidateRefreshToken(refreshToken, userTokenRepository);
            UserAuthenticationResponse authenticationResponse = new UserAuthenticationResponse();
            authenticationResponse.setAccessToken(token.getAccessToken());
            authenticationResponse.setRefreshToken(token.getRefreshToken());
            return ResponseUtil.getSuccessfulApiResponse(authenticationResponse, "Tokens refreshed successfully.");
        }
        return ResponseUtil.getFailureResponse("The refresh token is invalid. Please log in again to continue.");
    }

    @Override
    public ApiResponse<?> logout(String authHeader, HttpServletResponse response) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseUtil.getFailureResponse("The token is invalid.");
        }
        String token = authHeader.substring(7);
        UserToken storedToken = userTokenRepository.findByAccessTokenAndLoggedOutFalse(token).orElse(null);

        if (storedToken != null) {
            storedToken.setLoggedOut(true);
            userTokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
            return ResponseUtil.getSuccessfulApiResponse("You have been successfully logged out.");
        }
        return ResponseUtil.getFailureResponse("Logout failed. An error occurred.");
    }

    @Override
    public ApiResponse<?> checkAuthentication(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseUtil.getAuthenticatedApiResponse("AUTHENTICATED");
        }
        return ResponseUtil.getUnAuthorized("UNAUTHENTICATED");
    }

}
