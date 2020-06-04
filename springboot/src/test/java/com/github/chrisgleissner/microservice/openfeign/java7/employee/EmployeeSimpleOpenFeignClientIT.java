package com.github.chrisgleissner.microservice.openfeign.java7.employee;

import com.github.chrisgleissner.microservice.openfeign.java7.FeignClient;
import com.github.chrisgleissner.microservice.openfeign.java7.FeignClientUrls;
import com.github.chrisgleissner.microservice.openfeign.java7.security.AuthFixture;

public class EmployeeSimpleOpenFeignClientIT extends AbstractEmployeeIT {
    @Override protected EmployeeClient createClient() {
        return FeignClient.builder()
                .jwt(AuthFixture.getJwt())
                .build(EmployeeSimpleClient.class, FeignClientUrls.MICROSERVICE_URL);
    }
}