package com.github.chrisgleissner.microservice.openfeign.employee;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import java.util.List;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;

public interface EmployeeJaxrsClient {
    @GET @Path("/api/employee")
    List<Employee> findAll(@HeaderParam(AUTHORIZATION) String jwt);
}
