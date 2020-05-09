package com.github.chrisgleissner.microservice.springboot.security.jwt;

import com.github.chrisgleissner.microservice.springboot.security.auth.domain.UserCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.regex.Pattern;

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
        try {
            ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(JWTS_PATH,
                    new UserCredentials(username, password), String.class);
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            HttpHeaders headers = responseEntity.getHeaders();
            String jwt = assertJwtHeaderIsStructurallyCorrect(headers);
            log.info("Got JWT {} for {}", jwt, username);
            return headers;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get JWT for " + username, e);
        }
    }

    private static String assertJwtHeaderIsStructurallyCorrect(HttpHeaders headers) {
        List<String> authorizationValues = headers.get(JwtUtil.AUTHORIZATION_HEADER_NAME);
        assertThat(authorizationValues).hasSize(1);
        String authorizationValue = authorizationValues.get(0);
        assertThat(authorizationValue).startsWith(JwtUtil.AUTHORIZATION_TOKEN_PREFIX);
        String jwt = authorizationValue.replace(JwtUtil.AUTHORIZATION_TOKEN_PREFIX, "");
        assertThat(Pattern.compile("\\.").matcher(jwt).results().count()).isEqualTo(2);
        assertThat(jwt).hasSizeGreaterThan(100);
        return jwt;
    }
}
