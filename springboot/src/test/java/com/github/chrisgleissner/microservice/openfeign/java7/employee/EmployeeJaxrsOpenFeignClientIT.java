package com.github.chrisgleissner.microservice.openfeign.java7.employee;

import com.github.chrisgleissner.microservice.openfeign.java7.FeignFactory;
import com.github.chrisgleissner.microservice.openfeign.java7.security.AuthFixture;
import feign.jaxrs.JAXRSContract;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class EmployeeJaxrsOpenFeignClientIT {
    private static final String AUTHORIZATION_VALUE = "Bearer " + AuthFixture.getJwt();

    @Test
    public void findAll() {
        EmployeeJaxrsClient client = FeignFactory.createClient(EmployeeJaxrsClient.class, FeignFactory.DEFAULT_URL,
                FeignFactory.JACKSON_ENCODER, FeignFactory.JACKSON_DECODER, new JAXRSContract());
        List<Employee> employees = client.findAll(AUTHORIZATION_VALUE);
        assertThat(employees).isNotEmpty();
        for (Employee employee : employees) {
            assertThat(employee).hasNoNullFieldsOrProperties();
        }
    }
}