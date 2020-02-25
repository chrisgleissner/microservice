package com.github.chrisgleissner.microservice.quarkus.employee;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class EmployeeResourceTest {

    @Test
    void findById() {
        Employee employee = get("/api/employee/1").then()
                .assertThat()
                .statusCode(200)
                .extract().as(Employee.class);
        assertThat(employee.getFirstname()).isEqualTo("John");
    }

    @Test
    void findByIdFails() {
        String body = get("/api/employee/4").then()
                .assertThat()
                .statusCode(404).extract().asString();
        assertThat(body).isEmpty();
    }

    @Test
    void findAll() {
        List<Employee> employees = List.of(get("/api/employee").then()
                .assertThat()
                .statusCode(200)
                .extract().as(Employee[].class));
        assertThat(employees).hasSize(3);
        assertThat(employees).allSatisfy(e -> assertThat(e.getFirstname()).isNotNull());
    }

    @Test
    void findByLastName() {
        List<Employee> employees = List.of(get("/api/employee?lastName=doe").then()
                .assertThat()
                .statusCode(200)
                .extract().as(Employee[].class));
        assertThat(employees).isNotEmpty();
        assertThat(employees).allSatisfy(e -> assertThat(e.getLastname()).isEqualToIgnoringCase("doe"));
    }
}