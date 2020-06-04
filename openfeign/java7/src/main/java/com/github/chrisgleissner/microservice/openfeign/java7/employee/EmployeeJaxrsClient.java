package com.github.chrisgleissner.microservice.openfeign.java7.employee;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/micro/api/employee")
public interface EmployeeJaxrsClient extends EmployeeClient {
    @GET
    List<Employee> findAll();

    @GET @Path("/{id}")
    Employee findById(@PathParam("id") long id);
}
