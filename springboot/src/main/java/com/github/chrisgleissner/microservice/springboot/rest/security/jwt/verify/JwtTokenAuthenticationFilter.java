package com.github.chrisgleissner.microservice.springboot.rest.security.jwt.verify;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.chrisgleissner.microservice.springboot.rest.security.jwt.JwtConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor @Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        final String header = request.getHeader(JwtConfig.AUTHORIZATION_HEADER_NAME);
        if (header != null && header.startsWith(JwtConfig.AUTHORIZATION_TOKEN_PREFIX)) {
            try {
                final String token = header.replace(JwtConfig.AUTHORIZATION_TOKEN_PREFIX, "");
                activateAuthenticatedUser(Jwts.parser().setSigningKey(jwtConfig.getSecret().getBytes()).parseClaimsJws(token).getBody());
            } catch (Exception e) {
                SecurityContextHolder.clearContext();
            }
        }
        chain.doFilter(request, response);
    }

    private void activateAuthenticatedUser(Claims claims) {
        Optional.ofNullable(claims.getSubject()).ifPresent(username -> {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username, null,
                    ((List<String>) claims.get("authorities")).stream().map(SimpleGrantedAuthority::new).collect(toList())));
        });
    }
}