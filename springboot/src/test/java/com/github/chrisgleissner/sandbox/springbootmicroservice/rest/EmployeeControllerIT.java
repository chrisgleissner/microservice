package com.github.chrisgleissner.sandbox.springbootmicroservice.rest;

import com.github.chrisgleissner.sandbox.springbootmicroservice.entity.Employee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class) @RestIT
class EmployeeControllerIT {
    @Autowired TestRestTemplate template;

    @Test
    void findAll() {
        ResponseEntity<Employee[]> responseEntity = template.getForEntity("/api/employee", Employee[].class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotEmpty();
    }

    @Test
    void findByIdNotFoundReturnsJsonWithErrorMessage() {
        ResponseEntity<String> responseEntity = template.getForEntity("/api/employee/0", String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).contains("\"status\":404,\"error\":\"Not Found\"," +
                "\"message\":\"Can't find employee by ID 0\",\"path\":\"/micro/api/employee/0\"}");
    }
}
