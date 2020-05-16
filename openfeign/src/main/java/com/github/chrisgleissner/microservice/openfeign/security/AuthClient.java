package com.github.chrisgleissner.microservice.openfeign.security;

import feign.Headers;
import feign.RequestLine;

public interface AuthClient {
    @RequestLine("POST /api/auth/jwts")
    @Headers("Content-Type: application/json")
    String getJwt(UserCredentials userCredentials);
}
