package com.example.userservice.security;

import com.example.userservice.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityNew {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment env;

    public WebSecurityNew(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.env = env;
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // AuthenticationManager 설정
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
        AuthenticationManager authenticationManager = authManagerBuilder.build();

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(authz -> authz
                .requestMatchers(new AntPathRequestMatcher("/actuator/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/users", "POST")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/welcome")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/health-check")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/swagger-resources/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                // IP 조건을 단일 조건으로 통합합니다.
                .requestMatchers("/**").access(new WebExpressionAuthorizationManager(
                        "hasIpAddress('localhost') or hasIpAddress('127.0.0.1') or " +
                        "hasIpAddress('0.0.0.0/0') or hasIpAddress('172.18.0.0/16')"
                ))
                .anyRequest().authenticated()
        );

        http.authenticationManager(authenticationManager)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
            .addFilter(getAuthenticationFilter(authenticationManager));

        return http.build();
    }

    private AuthenticationFilterNew getAuthenticationFilter(AuthenticationManager authenticationManager) throws Exception {
        return new AuthenticationFilterNew(authenticationManager, userService, env);
    }
}
