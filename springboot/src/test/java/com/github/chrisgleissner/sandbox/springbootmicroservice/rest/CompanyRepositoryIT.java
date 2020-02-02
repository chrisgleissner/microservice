package com.github.chrisgleissner.sandbox.springbootmicroservice.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class) @RestIT
class CompanyRepositoryIT {

    @Autowired TestRestTemplate template;

    @Test
    void findAllReturnsHateaosResponse() {
        ResponseEntity<String> responseEntity = template.getForEntity("/api/company", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        String responseString = responseEntity.getBody();
        assertThat(responseString).startsWith("{\n  \"_embedded\" : {");
        assertThat(responseString).contains("Foo", "Bar");
    }
}