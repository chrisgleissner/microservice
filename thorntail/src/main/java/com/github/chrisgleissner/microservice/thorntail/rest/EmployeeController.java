package com.github.chrisgleissner.microservice.thorntail.rest;


import com.github.chrisgleissner.microservice.thorntail.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import java.util.Optional;


@Path("/api/employee")
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    @Inject
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response findById(@PathParam("id") Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Can't find employee by ID " + id));
    }
}
