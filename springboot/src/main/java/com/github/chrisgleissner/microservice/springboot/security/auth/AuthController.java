package com.github.chrisgleissner.microservice.springboot.security.auth;

import com.github.chrisgleissner.microservice.springboot.security.auth.domain.AuthorizationInfo;
import com.github.chrisgleissner.microservice.springboot.security.auth.domain.UserCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController @RequestMapping(value = "/api/auth", produces = TEXT_PLAIN_VALUE) @RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("jwts")
    public String getJwt(@RequestBody UserCredentials userCredentials) throws AuthenticationException {
        return authService.getJwt(userCredentials.getUsername(), userCredentials.getPassword());
    }

    @PostMapping("authorizations")
    public AuthorizationInfo getAuthorizationInfo(@RequestBody String jwt) throws AuthenticationException {
       return authService.getAuthInfo(jwt);
    }
}