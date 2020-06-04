package com.github.chrisgleissner.microservice.openfeign.java7.employee;

import feign.FeignException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public abstract class AbstractEmployeeIT {
    private final EmployeeClient client = createClient();

    protected abstract EmployeeClient createClient();

    @Test
    public void findAll() {
        List<Employee> employees = client.findAll();
        assertThat(employees).isNotEmpty();
        for (Employee employee : employees) {
            assertThat(employee).hasNoNullFieldsOrProperties();
        }
    }

    @Test
    public void findById() {
        Employee e = client.findById(1);
        assertThat(e.getFirstname()).isEqualTo("John");
    }

    @Test
    public void findByIdNoResults() {
        assertThatExceptionOfType(FeignException.class).isThrownBy(() -> client.findById(-1))
                .withMessageContaining("status 404 reading");
    }
}
