package com.github.chrisgleissner.microservice.springboot.employee;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static com.github.chrisgleissner.microservice.springboot.security.auth.jwt.JwtFixture.adminJwt;
import static com.github.chrisgleissner.microservice.springboot.security.auth.jwt.JwtFixture.userJwt;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

abstract class AbstractEmployeeControllerIT {
    private static final Employee EMPLOYEE = new Employee("Foo", "Bar",  LocalDate.of(2000, 1, 1));

    @Autowired TestRestTemplate template;

    @Test
    void findAll() {
        ResponseEntity<Employee[]> response = template.exchange("/api/employee", GET, new HttpEntity<>(userJwt(template)), Employee[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void findByIdNotFound() {
        ResponseEntity<String> response = template.exchange("/api/employee/0", GET, new HttpEntity<>(userJwt(template)), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("\"status\":404,\"error\":\"Not Found\"," +
                "\"message\":\"Can't find employee by ID 0\",\"path\":\"/micro/api/employee/0\"}");
    }

    @Test
    void create() {
        ResponseEntity<Employee> response = template.exchange("/api/employee", POST, new HttpEntity<>(EMPLOYEE, adminJwt(template)), Employee.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Employee createdEmployee = response.getBody();
        assertThat(createdEmployee).isEqualToIgnoringGivenFields(EMPLOYEE, "id");
        Long createdEmployeeId = createdEmployee.getId();
        assertThat(createdEmployeeId).isNotNull();

        assertThat(createdEmployee).isEqualTo(template.exchange("/api/employee/" + createdEmployeeId, GET,
                new HttpEntity<>(userJwt(template)), Employee.class).getBody());
    }

    @Test
    void createUnauthorized() {
        assertThat(template.exchange("/api/employee", POST, new HttpEntity<>(EMPLOYEE), Employee.class)
                .getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void createForbidden() {
        assertThat(template.exchange("/api/employee", POST, new HttpEntity<>(EMPLOYEE, userJwt(template)), Employee.class)
                .getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
