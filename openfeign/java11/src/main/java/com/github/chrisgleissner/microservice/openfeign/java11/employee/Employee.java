package com.github.chrisgleissner.microservice.openfeign.java11.employee;

import lombok.Value;
import java.time.LocalDate;

@Value
public class Employee {
    Long id;
    String firstname;
    String lastname;
    LocalDate birthday;
}
