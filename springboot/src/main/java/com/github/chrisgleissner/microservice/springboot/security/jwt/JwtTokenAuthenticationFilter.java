package com.github.chrisgleissner.microservice.springboot.security.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.github.chrisgleissner.microservice.springboot.security.jwt.JwtConfig.AUTHORITIES_CLAIM;
import static com.github.chrisgleissner.microservice.springboot.security.jwt.JwtConfig.AUTHORIZATION_HEADER_NAME;
import static com.github.chrisgleissner.microservice.springboot.security.jwt.JwtConfig.AUTHORIZATION_TOKEN_PREFIX;
import static java.util.stream.Collectors.toList;

/**
 * Verifies a valid JWT exists.
 */
@RequiredArgsConstructor @Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtManager jwtManager;

    static Optional<String> jwt(String headerValue) {
        return Optional.ofNullable(headerValue)
                .filter(hv -> hv.startsWith(AUTHORIZATION_TOKEN_PREFIX))
                .map(hv -> hv.substring(AUTHORIZATION_TOKEN_PREFIX.length()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            jwt(request.getHeader(AUTHORIZATION_HEADER_NAME)).ifPresent(jwt -> activateAuthenticatedUser(jwtManager.getClaims(jwt)));
        } catch (Exception e) {
            log.warn("Failed to authenticate user", e);
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }

    private void activateAuthenticatedUser(Claims claims) {
        Optional.ofNullable(claims.getSubject()).ifPresent(username -> {
            log.debug("Authenticating {}", username);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null,
                    ((List<String>) claims.get(AUTHORITIES_CLAIM)).stream().map(SimpleGrantedAuthority::new).collect(toList())));
        });
    }
}