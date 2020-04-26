package com.github.chrisgleissner.microservice.springboot.employee;

import com.github.chrisgleissner.microservice.springboot.rest.RestIT;
import com.github.chrisgleissner.microservice.springboot.rest.security.jwt.create.UserConstants;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static com.github.chrisgleissner.microservice.springboot.fixture.JsonFixture.fromJson;
import static com.github.chrisgleissner.microservice.springboot.fixture.JsonFixture.json;
import static com.github.chrisgleissner.microservice.springboot.fixture.JwtFixture.adminJwt;
import static com.github.chrisgleissner.microservice.springboot.fixture.JwtFixture.userJwt;
import static io.restassured.http.Cookie.PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@ExtendWith(SpringExtension.class) @RestIT
class EmployeeControllerIT {
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
