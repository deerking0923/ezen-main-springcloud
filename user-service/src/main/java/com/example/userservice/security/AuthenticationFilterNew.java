package com.example.userservice.security;

import com.example.userservice.dto.UserDto;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.RequestLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class AuthenticationFilterNew  extends UsernamePasswordAuthenticationFilter {

    private UserService userService;
    private Environment environment;

    public AuthenticationFilterNew(AuthenticationManager authenticationManager,
                                   UserService userService, Environment environment) {
        super(authenticationManager);
        this.userService = userService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {

            RequestLogin creds = new ObjectMapper().readValue(req.getInputStream(), RequestLogin.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                              Authentication auth) throws IOException, ServletException {
    
        String userName = ((User) auth.getPrincipal()).getUsername();
        UserDto userDetails = userService.getUserDetailsByEmail(userName);
    
        // token.secret 값을 환경 변수에서 읽어옵니다.
        String tokenSecret = environment.getProperty("token.secret");
        if (tokenSecret == null) {
            // 만약 token.secret이 null이면, 기본값 또는 예외 처리를 합니다.
            System.out.println("token.secret is null. Using default secret.");
            tokenSecret = "defaultSecretKey"; // 보안에 취약할 수 있으므로, 실제 배포시엔 안전한 기본값 사용 또는 오류 발생 처리
        } else {
            System.out.println("token.secret: " + tokenSecret);
        }
    
        // Base64 인코딩을 통해 SecretKey 생성
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
    
        Instant now = Instant.now();
    
        // token.expiration_time 값을 환경 변수에서 읽어오고, null인 경우 기본값(86400000ms, 24시간)을 사용
        String expirationTimeStr = environment.getProperty("token.expiration_time");
        long expirationTime = 86400000L; // 기본값 24시간
        if (expirationTimeStr != null) {
            try {
                expirationTime = Long.parseLong(expirationTimeStr);
            } catch (NumberFormatException e) {
                System.out.println("Error parsing token.expiration_time, using default 86400000");
                expirationTime = 86400000L;
            }
        } else {
            System.out.println("token.expiration_time is null. Using default 86400000");
        }
        System.out.println("token.expiration_time: " + expirationTime);
    
        // JWT 토큰 생성
        String token = Jwts.builder()
                .subject(userDetails.getUserId())
                .expiration(Date.from(now.plusMillis(expirationTime)))
                .issuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();
    
        // 응답 헤더에 JWT 토큰과 사용자 ID 추가
        res.addHeader("token", token);
        res.addHeader("userId", userDetails.getUserId());
    }
    
}
