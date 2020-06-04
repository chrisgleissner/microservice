package com.github.chrisgleissner.microservice.openfeign.java7.employee;

import feign.Param;
import feign.RequestLine;

import java.util.List;


public interface EmployeeSimpleClient extends EmployeeClient {
    @RequestLine("GET /micro/api/employee")
    List<Employee> findAll();

    @RequestLine("GET /micro/api/employee/{id}")
    Employee findById(@Param("id") long id);
}
