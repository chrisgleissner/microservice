package com.github.chrisgleissner.microservice.springboot.security;

import com.github.chrisgleissner.microservice.springboot.security.auth.AuthService;
import com.github.chrisgleissner.microservice.springboot.security.auth.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.chrisgleissner.microservice.springboot.security.auth.jwt.JwtUtil.AUTHORIZATION_HEADER_NAME;
import static java.util.stream.Collectors.toList;

/**
 * Verifies a valid JWT exists.
 */
@Component @RequiredArgsConstructor @Slf4j
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        JwtUtil.fromHeader(request.getHeader(AUTHORIZATION_HEADER_NAME))
                .ifPresentOrElse(jwt -> {
                    try {
                        activateAuthenticatedUser(jwt);
                    } catch (AuthenticationException e) {
                        log.warn("Failed to authenticate user", e);
                        SecurityContextHolder.clearContext();
                    }
                }, SecurityContextHolder::clearContext);
        chain.doFilter(request, response);
    }

    private void activateAuthenticatedUser(String jwt) throws AuthenticationException {
        val authorizationInfo = authService.getAuthorizationInfo(jwt);
        val grantedAuthorities = authorizationInfo.getRoles().stream()
                .map(JwtTokenAuthenticationFilter::springRoleName)
                .map(SimpleGrantedAuthority::new).collect(toList());
        val authenticationToken = new UsernamePasswordAuthenticationToken(authorizationInfo.getUsername(), null, grantedAuthorities);
        log.debug("Authenticating {}", authorizationInfo.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private static String springRoleName(String roleName) {
        return "ROLE_" + roleName;
    }
}