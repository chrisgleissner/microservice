package com.github.chrisgleissner.microservice.openfeign.java7.employee;

import com.github.chrisgleissner.microservice.openfeign.java7.FeignClientFactory;
import com.github.chrisgleissner.microservice.openfeign.java7.FeignClientUrls;
import com.github.chrisgleissner.microservice.openfeign.java7.security.AuthFixture;
import feign.jaxrs.JAXRSContract;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class EmployeeJaxrsOpenFeignClientIT {
    private static final String AUTHORIZATION_VALUE = "Bearer " + AuthFixture.getJwt();

    @Test
    public void findAll() {
        EmployeeJaxrsClient client = FeignClientFactory.builder().contract(new JAXRSContract())
                .build(EmployeeJaxrsClient.class, FeignClientUrls.MICROSERVICE_URL);
        assertFindAllWorks(client);
    }

    private void assertFindAllWorks(EmployeeJaxrsClient client) {
        for (int i = 0; i < 10; i++) {
            long startTime = System.nanoTime();
            List<Employee> employees = client.findAll(AUTHORIZATION_VALUE);
            long durationInMillis = (System.nanoTime() - startTime) / 1000000;
            log.info("findAll() returned {} result(s) in {}ms", employees.size(), durationInMillis);
            assertThat(employees).isNotEmpty();
            for (Employee employee : employees) {
                assertThat(employee).hasNoNullFieldsOrProperties();
            }
        }
    }
}