package com.tansen.government.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tansen.common.dto.ApiResponse;
import com.tansen.common.dto.ResponseUtil;
import com.tansen.entity.AuthorityUserToken;
import com.tansen.repository.AuthorityUserTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class CustomLogoutHandler implements LogoutHandler{
    private static final Logger LOG = LoggerFactory.getLogger(CustomLogoutHandler.class);
    private final ObjectMapper objectMapper;
    private final AuthorityUserTokenRepository authorityUserTokenRepository;

    public CustomLogoutHandler(ObjectMapper objectMapper, AuthorityUserTokenRepository vendorUserTokenRepository) {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.authorityUserTokenRepository =vendorUserTokenRepository;
    }
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        response.setContentType("application/json");
        ApiResponse<?> apiResponse;
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            apiResponse = ResponseUtil.getFailureResponse("Invalid token");
            writeResponse(response, apiResponse);
            return;
        }

        String token = authHeader.substring(7);
        AuthorityUserToken storedToken = authorityUserTokenRepository.findByAccessTokenAndLoggedOutFalse(token).orElse(null);

        if (storedToken != null) {
            storedToken.setLoggedOut(true);
            authorityUserTokenRepository.save(storedToken);
            LOG.info("Logged out successfully");
            apiResponse = ResponseUtil.getSuccessfulApiResponse("Logged out successfully");
            writeResponse(response, apiResponse);
            return;
        }
        apiResponse = ResponseUtil.getFailureResponse("Logout Unsuccessful");
        writeResponse(response, apiResponse);
    }

    private void writeResponse(HttpServletResponse response, ApiResponse<?> apiResponse) {
        response.setContentType("application/json");
        try {
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
