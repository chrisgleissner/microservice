package com.github.chrisgleissner.microservice.springboot.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;

import java.util.Optional;


public interface JwtUtil {
    String AUTHORIZATION_HEADER_NAME = "Authorization";
    String AUTHORIZATION_TOKEN_PREFIX = "Bearer ";
    String AUTHORITIES_CLAIM = "authorities";

    static String getUsername(Claims claims) {
        return claims.getSubject();
    }

    static HttpHeaders toHeader(String jwt) {
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.set(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_TOKEN_PREFIX + jwt);
        return headers;
    }

    static Optional<String> fromHeader(String headerValue) {
        return Optional.ofNullable(headerValue)
                .filter(hv -> hv.startsWith(AUTHORIZATION_TOKEN_PREFIX))
                .map(hv -> hv.substring(AUTHORIZATION_TOKEN_PREFIX.length()));
    }
}
