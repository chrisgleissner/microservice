package com.github.chrisgleissner.microservice.quarkus.employee;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Entity @Value @AllArgsConstructor @NoArgsConstructor(access = PRIVATE, force = true)
public class Employee {
    @Id @GeneratedValue Long id;
    String firstname;
    String lastname;
    LocalDate birthday;
}
