package com.github.chrisgleissner.microservice.quarkus.employee;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class EmployeeTest {

    @Transactional
    @Test
    public void canPersistAndFindById() {
        Employee e = new Employee();
        e.id = 10L;
        e.firstname = "Tom";
        e.lastname = "Jones";
        e.birthday = LocalDate.of(1970, 1, 1);
        e.persist();

        Employee e2 = Employee.findById(10L).get();
        assertThat(e2.firstname).isEqualTo("Tom");
        assertThat(e2.lastname).isEqualTo("Jones");

    }

    @Test
    public void canGetEmployees() {
        List<Employee> employees = Employee.findByLastName("Doe");
        assertThat(employees).hasSize(2);
    }
}
