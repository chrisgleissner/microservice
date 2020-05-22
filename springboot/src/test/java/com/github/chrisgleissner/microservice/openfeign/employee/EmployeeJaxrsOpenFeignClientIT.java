package com.github.chrisgleissner.microservice.openfeign.employee;

import com.github.chrisgleissner.microservice.openfeign.FeignFactory;
import com.github.chrisgleissner.microservice.openfeign.security.AuthFixture;
import feign.jaxrs.JAXRSContract;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.chrisgleissner.microservice.openfeign.TestConstants.SERVICE_URL;
import static org.assertj.core.api.Assertions.assertThat;

// TODO Start up service and rename to IT
public class EmployeeJaxrsOpenFeignClientIT {
    private static final String AUTHORIZATION_VALUE = "Bearer " + AuthFixture.getJwt(SERVICE_URL);

    @Test
    public void findAll() {
        EmployeeJaxrsClient client = FeignFactory.createClient(EmployeeJaxrsClient.class, SERVICE_URL,
                FeignFactory.JACKSON_ENCODER, FeignFactory.JACKSON_DECODER, new JAXRSContract());
        List<Employee> employees = client.findAll(AUTHORIZATION_VALUE);
        assertThat(employees).isNotEmpty();
        for (Employee employee : employees) {
            assertThat(employee).hasNoNullFieldsOrProperties();
        }
    }
}