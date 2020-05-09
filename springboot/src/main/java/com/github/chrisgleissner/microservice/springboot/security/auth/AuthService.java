package com.github.chrisgleissner.microservice.springboot.security.auth;

import com.github.chrisgleissner.microservice.springboot.security.auth.domain.AuthorizationInfo;
import lombok.val;

import javax.naming.AuthenticationException;
import java.util.Set;

public interface AuthService {
    String getJwt(String username, String password) throws AuthenticationException;
    String getUsername(String jwt) throws AuthenticationException;
    Set<String> getRoles(String username);

    default AuthorizationInfo getAuthInfo(String jwt) throws AuthenticationException {
        val username = getUsername(jwt);
        return new AuthorizationInfo(username, getRoles(username));
    }

}
