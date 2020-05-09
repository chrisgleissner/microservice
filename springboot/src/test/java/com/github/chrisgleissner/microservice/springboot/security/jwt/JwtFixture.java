package com.github.chrisgleissner.microservice.springboot.security.jwt;

import com.github.chrisgleissner.microservice.springboot.security.auth.domain.UserCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.github.chrisgleissner.microservice.springboot.security.auth.AuthControllerConstants.JWTS_PATH;
import static com.github.chrisgleissner.microservice.springboot.security.auth.user.UserConstants.ADMIN_APP_USER;
import static com.github.chrisgleissner.microservice.springboot.security.auth.user.UserConstants.NORMAL_APP_USER;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class JwtFixture {

    public static HttpHeaders userJwt(TestRestTemplate testRestTemplate) {
        return jwt(testRestTemplate, NORMAL_APP_USER.getUsername(), NORMAL_APP_USER.getPassword());
    }

    public static HttpHeaders adminJwt(TestRestTemplate testRestTemplate) {
        return jwt(testRestTemplate, ADMIN_APP_USER.getUsername(), ADMIN_APP_USER.getPassword());
    }

    private static HttpHeaders jwt(TestRestTemplate testRestTemplate, String username, String password) {
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(JWTS_PATH,
                new UserCredentials(username, password), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        String jwt = responseEntity.getBody();
        assertThat(jwt).isNotEmpty();
        log.info("Got JWT {} for {}", jwt, username);
        return JwtUtil.toHeader(jwt);
    }
}
