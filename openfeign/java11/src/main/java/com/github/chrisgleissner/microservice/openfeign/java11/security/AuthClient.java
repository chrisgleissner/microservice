package com.github.chrisgleissner.microservice.openfeign.java11.security;

import feign.Headers;
import feign.RequestLine;

public interface AuthClient {
    @Headers("Content-Type: application/json")
    @RequestLine("POST /api/auth/jwts")
    String getJwt(UserCredentials userCredentials);
}
