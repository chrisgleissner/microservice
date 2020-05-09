package com.github.chrisgleissner.microservice.springboot.security.auth;

import com.github.chrisgleissner.microservice.springboot.security.auth.domain.AuthorizationInfo;

import javax.naming.AuthenticationException;

public interface AuthService {
    String getJwt(String username, String password) throws AuthenticationException;
    AuthorizationInfo getAuthorizationInfo(String jwt) throws AuthenticationException;
}
