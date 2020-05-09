package com.github.chrisgleissner.microservice.springboot.security.auth;

import com.github.chrisgleissner.microservice.springboot.security.auth.util.AuthenticationManagerUtil;
import com.github.chrisgleissner.microservice.springboot.security.jwt.JwtManager;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.toCollection;

@Service @RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtManager jwtManager;

    @Override public String getUsername(String jwt) {
        return JwtManager.getUsername(jwtManager.getClaims(jwt));
    }

    @Override public String getJwt(String username, String password) {
        val authentication = AuthenticationManagerUtil.attemptAuthentication(authenticationManager, username, password);
        return jwtManager.createJwt(username, AuthenticationManagerUtil.getRoles(authentication));
    }

    @Override public Set<String> getRoles(String username) {
        return userDetailsService.loadUserByUsername(username).getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(toCollection(TreeSet::new));
    }
}
