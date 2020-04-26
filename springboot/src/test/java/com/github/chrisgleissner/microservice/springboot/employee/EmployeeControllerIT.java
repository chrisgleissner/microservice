package com.github.chrisgleissner.microservice.springboot.employee;

import com.github.chrisgleissner.microservice.springboot.rest.RestIT;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.github.chrisgleissner.microservice.springboot.rest.JwtFixture.userJwt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

@ExtendWith(SpringExtension.class) @RestIT
class EmployeeControllerIT {
    @Autowired TestRestTemplate template;

    @Test
    void findAll() {
        ResponseEntity<Employee[]> response = template.exchange("/api/employee", GET, new HttpEntity<>(userJwt(template)), Employee[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void findByIdNotFoundReturnsJsonWithErrorMessage() {
        ResponseEntity<String> response = template.exchange("/api/employee/0", GET, new HttpEntity<>(userJwt(template)), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("\"status\":404,\"error\":\"Not Found\"," +
                "\"message\":\"Can't find employee by ID 0\",\"path\":\"/micro/api/employee/0\"}");
    }
}
