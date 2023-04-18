package com.example.oauthstarter.infrastructure.configuration;

import com.example.oauthstarter.domain.service.AuthUserDetailsService;
import com.example.oauthstarter.infrastructure.oauth.CustomOAuthUserService;
import com.example.oauthstarter.infrastructure.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.oauthstarter.infrastructure.oauth.OAuth2AuthenticationFailureHandler;
import com.example.oauthstarter.infrastructure.oauth.OAuth2AuthenticationSuccessHandler;
import com.example.oauthstarter.infrastructure.security.AuthEntryPoint;
import com.example.oauthstarter.infrastructure.security.TokenAuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class GlobalAppSecurityConfiguration {
    private static final String[] WHITE_LIST = {
            "/",
            "/error",
            "/favicon.ico",
            "/**/*.png",
            "/**/*.gif",
            "/**/*.svg",
            "/**/*.jpg",
            "/**/*.html",
            "/**/*.css",
            "/**/*.js"};
    private final AuthUserDetailsService authUserDetailsService;
    private final CustomOAuthUserService customOAuthUserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public TokenAuthFilter tokenAuthenticationFilter() {
        return new TokenAuthFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new AuthEntryPoint());
        http.authorizeHttpRequests()
                .requestMatchers(WHITE_LIST)
                    .permitAll()
                .requestMatchers("/auth/**", "/oauth2/**")
                    .permitAll()
                .anyRequest()
                .authenticated();
        http.oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuthUserService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);
        return http.build();
    }

    private AuthorizationRequestRepository<OAuth2AuthorizationRequest> cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }
}
