package com.github.chrisgleissner.microservice.openfeign.java7.employee;

import java.util.List;

public interface EmployeeClient {
    List<Employee> findAll();
    Employee findById(long id);
}
