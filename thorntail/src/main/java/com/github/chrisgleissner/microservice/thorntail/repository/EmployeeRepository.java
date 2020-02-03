package com.github.chrisgleissner.microservice.thorntail.repository;


import com.github.chrisgleissner.microservice.thorntail.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("select u from Employee u where lower(u.lastname) = lower(?1)")
    Iterable<Employee> findByLastname(String lastname);
}
