package com.github.chrisgleissner.microservice.quarkus.employee;

import lombok.RequiredArgsConstructor;
import org.eclipse.microprofile.metrics.annotation.Timed;

import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static org.eclipse.microprofile.metrics.MetricUnits.MILLISECONDS;

@Path("employee")
@Produces(MediaType.APPLICATION_JSON) @RequiredArgsConstructor
public class EmployeeResource {
    private final EntityManager em;

    @Timed(name = "findById", unit = MILLISECONDS)
    @GET @Path("/{id}")
    public Employee findById(@PathParam("id") Long id) {
        return Optional.ofNullable(em.find(Employee.class, id))
                .orElseThrow(() -> new WebApplicationException("Can't find employee by ID " + id, Response.Status.NOT_FOUND));
    }

    @GET
    public List<Employee> findAll(@QueryParam("lastName") String lastName) {
        return Optional.ofNullable(lastName)
                .map(n -> em.createQuery("from Employee u where lower(u.lastname) = lower(?1)", Employee.class).setParameter(1, n))
                .orElseGet(() -> em.createQuery("from Employee", Employee.class)).getResultList();
    }

    @GET
    public List<Employee> employees() {
        return em.createQuery("from Employee", Employee.class).getResultList();
    }
}