package com.example.oauthstarter.infrastructure.security;

import com.example.oauthstarter.domain.model.AuthUserDetails;
import com.example.oauthstarter.domain.service.AuthUserService;
import com.example.oauthstarter.infrastructure.utils.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class AuthFilter extends OncePerRequestFilter {
    @Autowired
    private AuthUserService userService;

    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = extractJwt(request);
            if (tokenProvider.validate(token)) {
                String username = tokenProvider.getUsernameFromJWT(token);
                if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                    AuthUserDetails authUser = userService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    private String extractJwt(HttpServletRequest request) {
        final String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header)) {
            return header.startsWith("Bearer ") ? header.substring(7) : header;
        }
        return null;
    }
}
