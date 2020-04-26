package com.github.chrisgleissner.microservice.springboot.rest.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.github.chrisgleissner.microservice.springboot.rest.security.jwt.JwtConfig.AUTHORITIES_CLAIM;
import static com.github.chrisgleissner.microservice.springboot.rest.security.jwt.JwtConfig.AUTHORIZATION_HEADER_NAME;
import static com.github.chrisgleissner.microservice.springboot.rest.security.jwt.JwtConfig.AUTHORIZATION_TOKEN_PREFIX;
import static com.github.chrisgleissner.microservice.springboot.rest.security.jwt.JwtConfig.LOGIN_PATH;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Creates a JWT based on a username and password.
 */
@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authManager;
    private final JwtConfig jwtConfig;

    public JwtUsernameAndPasswordAuthenticationFilter(ObjectMapper objectMapper, AuthenticationManager authManager, JwtConfig jwtConfig) {
        this.objectMapper = objectMapper;
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;
        setFilterProcessesUrl(LOGIN_PATH);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            final UserCredentials creds = objectMapper.readValue(request.getInputStream(), UserCredentials.class);
            return authManager.authenticate(new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword(), emptyList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param authenticatedUser obtained from UserDetailsServiceImpl
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authenticatedUser) {
        final long millisSinceEpoch = System.currentTimeMillis();
        final String token = Jwts.builder()
                .setSubject(authenticatedUser.getName())
                .claim(AUTHORITIES_CLAIM, authenticatedUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toList()))
                .setIssuedAt(new Date(millisSinceEpoch))
                .setExpiration(new Date(millisSinceEpoch + jwtConfig.getExpiration() * 1000))  // in milliseconds
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
