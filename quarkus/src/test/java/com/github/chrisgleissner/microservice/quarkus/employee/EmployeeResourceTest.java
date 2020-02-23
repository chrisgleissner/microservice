package com.github.chrisgleissner.microservice.quarkus.employee;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class EmployeeResourceTest {

    @Disabled("TODO Fails with 'did you forget to annotate your entity with @Entity?'")
    @Test
    void findAll() {
        List<Employee> employees = List.of(get("/api/employee").then()
                .assertThat()
                .statusCode(200)
                .extract().as(Employee[].class));
        assertThat(employees).isNotEmpty();
    }
}