package com.github.chrisgleissner.microservice.quarkus.employee;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("employee") @Produces(MediaType.APPLICATION_JSON)
public class EmployeeResource {

	@GET @Path("/{id}")
	public Employee findById(@PathParam("{id}") Long id) {
		return Employee.findById(id).orElseThrow(() -> new WebApplicationException("Can't find employee by ID " + id, Response.Status.NOT_FOUND));
	}

	@GET
	public Iterable<Employee> findAll(@QueryParam("lastName") String lastName) {
		return Optional.ofNullable(lastName).map(Employee::findByLastName).orElseGet(Employee::listAll);
	}
}