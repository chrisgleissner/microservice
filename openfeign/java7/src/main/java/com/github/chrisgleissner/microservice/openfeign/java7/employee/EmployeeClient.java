package com.github.chrisgleissner.microservice.openfeign.java7.employee;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

public interface EmployeeClient {
    @Headers("Authorization: Bearer {jwt}")
    @RequestLine("GET /micro/api/employee")
    List<Employee> findAll(@Param("jwt") String jwt);;
}
