package com.github.chrisgleissner.microservice.openfeign.java7.employee;

import com.github.chrisgleissner.microservice.openfeign.java7.FeignFactory;
import com.github.chrisgleissner.microservice.openfeign.java7.security.AuthFixture;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeOpenFeignClientIT {
    private static final String JWT = AuthFixture.getJwt();

    @Test
    public void findAllViaLoadBalancer() {
        for (int i = 0; i < 3; i++) {
            assertFindAllWorks();
        }
    }

    private void assertFindAllWorks() {
        EmployeeClient client = FeignFactory.createClient(EmployeeClient.class);
        List<Employee> employees = client.findAll(JWT);
        assertThat(employees).isNotEmpty();
        for (Employee employee : employees) {
            assertThat(employee).hasNoNullFieldsOrProperties();
        }
    }
}