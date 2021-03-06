package com.github.chrisgleissner.microservice.openfeign.java7.employee;

import com.github.chrisgleissner.microservice.openfeign.java7.FeignClient;
import com.github.chrisgleissner.microservice.openfeign.java7.FeignClientUrls;
import com.github.chrisgleissner.microservice.openfeign.java7.security.AuthFixture;
import feign.jaxrs.JAXRSContract;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmployeeJaxrsOpenFeignClientIT extends AbstractEmployeeIT {

    @Override protected EmployeeClient createClient() {
        return FeignClient.builder()
                .contract(new JAXRSContract())
                .jwt(AuthFixture.getJwt())
                .build(EmployeeJaxrsClient.class, FeignClientUrls.MICROSERVICE_URL);
    }
}