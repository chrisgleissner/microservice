package com.github.chrisgleissner.microservice.openfeign.employee;

import com.github.chrisgleissner.microservice.openfeign.FeignFactory;
import com.github.chrisgleissner.microservice.openfeign.security.AuthClient;
import com.github.chrisgleissner.microservice.openfeign.security.AuthFixture;
import com.github.chrisgleissner.microservice.openfeign.security.ImmutableUserCredentials;
import feign.codec.StringDecoder;
import feign.jackson.JacksonEncoder;
import feign.jaxrs.JAXRSContract;
import org.junit.Test;

import java.util.List;

import static com.github.chrisgleissner.microservice.openfeign.OpenFeignTestConstants.SERVICE_URL;
import static org.assertj.core.api.Assertions.assertThat;

// TODO Start up service and rename to IT
public class EmployeeClientMT {
    private static final String JWT = AuthFixture.getJwt(SERVICE_URL);

    @Test
    public void findAll() {
        EmployeeClient client = FeignFactory.createClient(EmployeeClient.class, SERVICE_URL);
        List<Employee> employees = client.findAll(JWT);
        assertThat(employees).isNotEmpty();
        for (Employee employee : employees) {
            assertThat(employee).hasNoNullFieldsOrProperties();
        }
    }
}