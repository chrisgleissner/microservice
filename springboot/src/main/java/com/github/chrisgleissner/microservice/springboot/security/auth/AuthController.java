package com.github.chrisgleissner.microservice.springboot.security.auth;

import com.github.chrisgleissner.microservice.springboot.security.auth.domain.AuthorizationInfo;
import com.github.chrisgleissner.microservice.springboot.security.auth.domain.UserCredentials;
import com.github.chrisgleissner.microservice.springboot.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

import static com.github.chrisgleissner.microservice.springboot.security.auth.AuthControllerConstants.JWTS_SUBPATH;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@RestController @RequestMapping(value = AuthControllerConstants.AUTH_PATH, produces = TEXT_PLAIN_VALUE) @RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping(path = JWTS_SUBPATH, consumes = APPLICATION_JSON_VALUE, produces = TEXT_PLAIN_VALUE)
    public String getJwt(@RequestBody UserCredentials userCredentials) throws AuthenticationException {
        return authService.getJwt(userCredentials.getUsername(), userCredentials.getPassword());
    }

    @PostMapping(path = "authorizations", consumes = TEXT_PLAIN_VALUE, produces = APPLICATION_JSON_VALUE)
    public AuthorizationInfo getAuthorizationInfo(@RequestBody String jwt) throws AuthenticationException {
       return authService.getAuthInfo(jwt);
    }
}