package com.example.oauthstarter.infrastructure.configuration;

import com.example.oauthstarter.infrastructure.oauth.CustomOAuthUserService;
import com.example.oauthstarter.infrastructure.oauth.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.oauthstarter.infrastructure.oauth.OAuth2AuthenticationFailureHandler;
import com.example.oauthstarter.infrastructure.oauth.OAuth2AuthenticationSuccessHandler;
import com.example.oauthstarter.infrastructure.security.AuthEntryPoint;
import com.example.oauthstarter.infrastructure.security.AuthFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


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
            "/**/*.js"
    };
    private final CustomOAuthUserService customOAuthUserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final AuthFilter authFilter;
    private final AuthEntryPoint authEntryPoint;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(new AntPathRequestMatcher("/auth/**"))
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authenticationProvider(authenticationProvider)
                    .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

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
