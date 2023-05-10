package com.example.oauthstarter.domain.service;

import com.example.oauthstarter.domain.model.AuthUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
@AllArgsConstructor
public class AuthLogoutHandler implements LogoutHandler {
    TokenService tokenService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        if (Objects.nonNull(authentication)) {
            var userId = ((AuthUserDetails) authentication.getPrincipal()).getId();
            tokenService.deleteToken(userId);
            securityContextLogoutHandler.logout(request, response, authentication);
            SecurityContextHolder.clearContext();
        } else {
            // User is not authenticated
            log.info("User is not authenticated");
        }
    }
}
