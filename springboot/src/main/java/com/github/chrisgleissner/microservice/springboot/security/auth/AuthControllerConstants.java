package com.github.chrisgleissner.microservice.springboot.security.auth;

public interface AuthControllerConstants {
    String AUTH_PATH = "/api/auth";
    String JWTS_SUBPATH = "jwts";
    String JWTS_PATH = AUTH_PATH + "/" + JWTS_SUBPATH;
}
