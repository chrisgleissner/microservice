package com.github.chrisgleissner.microservice.springboot.security.auth.util;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.TreeSet;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toCollection;

public interface AuthenticationManagerUtil {

    static Authentication attemptAuthentication(AuthenticationManager authenticationManager, String username, String password) throws AuthenticationException {
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, emptyList()));
    }

    static Set<String> getRoles(Authentication authentication) throws AuthenticationException {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toCollection(TreeSet::new));
    }
}
