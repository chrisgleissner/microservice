package com.github.chrisgleissner.microservice.openfeign.java7.employee;

import com.github.chrisgleissner.microservice.openfeign.java7.FeignClientFactory;
import com.github.chrisgleissner.microservice.openfeign.java7.FeignClientUrls;
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
        EmployeeClient client = FeignClientFactory.builder().build(EmployeeClient.class, FeignClientUrls.MICROSERVICE_URL);
        List<Employee> employees = client.findAll(JWT);
        assertThat(employees).isNotEmpty();
        for (Employee employee : employees) {
            assertThat(employee).hasNoNullFieldsOrProperties();
        }
    }
}