package com.github.chrisgleissner.microservice.springboot.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("security.jwt") @Configuration @Data
public class JwtConfig {
    static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    static final String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    static final String AUTHORITIES_CLAIM = "authorities";

    private int expiration;
    private String secret;
}