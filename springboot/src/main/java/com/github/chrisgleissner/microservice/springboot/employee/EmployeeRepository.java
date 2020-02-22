package com.github.chrisgleissner.microservice.springboot.employee;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    @Query("select u from Employee u where lower(u.lastname) = lower(?1)")
    Iterable<Employee> findByLastname(String lastname);
}
