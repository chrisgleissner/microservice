package com.github.chrisgleissner.microservice.springboot.security.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration @NoArgsConstructor @Getter
public class JwtConfig {
    public static final String LOGIN_PATH = "/api/auth/login";
    static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    static final String AUTHORITIES_CLAIM = "authorities";

    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int expiration;

    @Value("${security.jwt.secret:JwtSecretKey}")
    private String secret;
}