package com.github.chrisgleissner.microservice.openfeign.java7.employee;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import java.util.List;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

public interface EmployeeJaxrsClient {
    @GET @Path("/micro/api/employee")
    List<Employee> findAll(@HeaderParam(AUTHORIZATION) String jwt);
}
