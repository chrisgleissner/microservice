package com.github.chrisgleissner.microservice.springboot.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chrisgleissner.microservice.springboot.security.auth.util.AuthenticationManagerUtil;
import com.github.chrisgleissner.microservice.springboot.security.auth.domain.UserCredentials;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.github.chrisgleissner.microservice.springboot.security.SecurityConstants.LOGIN_PATH;
import static com.github.chrisgleissner.microservice.springboot.security.jwt.JwtConfig.AUTHORIZATION_HEADER_NAME;
import static com.github.chrisgleissner.microservice.springboot.security.jwt.JwtConfig.AUTHORIZATION_TOKEN_PREFIX;
import static java.util.stream.Collectors.toSet;

/**
 * Creates a JWT based on a username and password.
 */
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtManager jwtManager;

    public JwtUsernameAndPasswordAuthenticationFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager, JwtManager jwtManager) {
        this.objectMapper = objectMapper;
        this.authenticationManager = authenticationManager;
        this.jwtManager = jwtManager;
        setFilterProcessesUrl(LOGIN_PATH);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            val userCredentials = objectMapper.readValue(request.getInputStream(), UserCredentials.class);
            return AuthenticationManagerUtil.attemptAuthentication(authenticationManager, userCredentials.getUsername(), userCredentials.getPassword());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param authenticatedUser obtained from UserDetailsServiceImpl
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authenticatedUser) {
        val jwt = jwtManager.createJwt(authenticatedUser.getName(),
                authenticatedUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(toSet()));
        response.addHeader(AUTHORIZATION_HEADER_NAME, AUTHORIZATION_TOKEN_PREFIX + jwt);
    }
}
