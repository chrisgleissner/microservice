package com.github.chrisgleissner.microservice.quarkus.employee;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity @Data
public class Employee {
    @Id @GeneratedValue Long id;
    String firstname;
    String lastname;
    LocalDate birthday;
}
