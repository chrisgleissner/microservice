package com.github.chrisgleissner.microservice.springboot.security.auth.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("security.jwt") @Configuration @Data
public class JwtConfig {
    private int expiration;
    private String secret;
    private boolean encodeRolesInJwt;
}