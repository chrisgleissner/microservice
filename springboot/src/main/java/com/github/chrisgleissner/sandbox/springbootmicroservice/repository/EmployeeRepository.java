package com.github.chrisgleissner.sandbox.springbootmicroservice.repository;

import com.github.chrisgleissner.sandbox.springbootmicroservice.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    @Query("select u from Employee u where lower(u.lastname) = lower(?1)")
    Iterable<Employee> findByLastname(String lastname);
}
