package com.github.chrisgleissner.microservice.springboot.company;

import com.github.chrisgleissner.microservice.springboot.rest.RestIT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.chrisgleissner.microservice.springboot.security.jwt.JwtFixture.userJwt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

@ExtendWith(SpringExtension.class) @RestIT
class CompanyRepositoryIT {

    @Autowired TestRestTemplate template;

    @Test
    void findAllReturnsHateaosResponse() {
        ResponseEntity<String> response = template.exchange("/api/company", GET, new HttpEntity<>(userJwt(template)), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String responseString = response.getBody();
        assertThat(responseString).startsWith("{\n  \"_embedded\" : {");
        assertThat(responseString).contains("Foo", "Bar");
    }
}