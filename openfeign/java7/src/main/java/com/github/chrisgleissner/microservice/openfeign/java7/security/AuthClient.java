package com.github.chrisgleissner.microservice.openfeign.java7.security;

import feign.Headers;
import feign.RequestLine;

public interface AuthClient {
    @Headers("Content-Type: application/json")
    @RequestLine("POST /micro/api/auth/jwts")
    String getJwt(UserCredentials userCredentials);
}
