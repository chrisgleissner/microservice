package com.github.chrisgleissner.sandbox.springbootmicroservice.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Value @AllArgsConstructor @NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
public class Employee {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) long id;
    String firstname;
    String lastname;
    LocalDate birthday;
}
