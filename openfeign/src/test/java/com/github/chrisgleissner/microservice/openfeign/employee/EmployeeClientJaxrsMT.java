package com.github.chrisgleissner.microservice.openfeign.employee;

import com.github.chrisgleissner.microservice.openfeign.FeignFactory;
import com.github.chrisgleissner.microservice.openfeign.security.AuthFixture;
import feign.jaxrs.JAXRSContract;
import org.junit.Test;

import java.util.List;

import static com.github.chrisgleissner.microservice.openfeign.OpenFeignTestConstants.SERVICE_URL;
import static org.assertj.core.api.Assertions.assertThat;

// TODO Start up service and rename to IT
public class EmployeeClientJaxrsMT {
    private static final String JWT = AuthFixture.getJwt(SERVICE_URL);

    @Test
    public void findAll() {
        EmployeeClientJaxrs client = FeignFactory.createClient(EmployeeClientJaxrs.class, SERVICE_URL, FeignFactory.JACKSON_ENCODER,
                FeignFactory.JACKSON_DECODER, new JAXRSContract());
        List<Employee> employees = client.findAll("Bearer " + JWT);
        assertThat(employees).isNotEmpty();
        for (Employee employee : employees) {
            assertThat(employee).hasNoNullFieldsOrProperties();
        }
    }
}