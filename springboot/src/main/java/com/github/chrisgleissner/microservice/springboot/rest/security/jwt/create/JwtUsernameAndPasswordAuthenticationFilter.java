package com.github.chrisgleissner.microservice.springboot.rest.security.jwt.create;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chrisgleissner.microservice.springboot.rest.security.jwt.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

import static com.github.chrisgleissner.microservice.springboot.rest.security.jwt.JwtConfig.AUTHORIZATION_HEADER_NAME;
import static com.github.chrisgleissner.microservice.springboot.rest.security.jwt.JwtConfig.AUTHORIZATION_TOKEN_PREFIX;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

// username/pwd -> jwt token. Listens on /login by default.
@RequiredArgsConstructor @Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter   {
    private final AuthenticationManager authManager;
    private final JwtConfig jwtConfig;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("Invoked attemptAuthentication()");
        try {
            UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);
            return authManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword(), emptyList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // authenticatedUser is obtained from UserDetailsServiceImpl
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authenticatedUser) {
        log.info("Invoked successfulAuthentication()");
        Long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject(authenticatedUser.getName())
                .claim("authorities", authenticatedUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();
        response.addHeader(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_TOKEN_PREFIX + token);
    }

    @Value @NoArgsConstructor(force = true) @RequiredArgsConstructor
    public static class UserCredentials {
        String username;
        String password;
    }
}
