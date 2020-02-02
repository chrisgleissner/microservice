package com.github.chrisgleissner.sandbox.springbootmicroservice.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

import static javax.persistence.EnumType.ORDINAL;

@Entity
@Table(name = "company")
@Value @AllArgsConstructor @NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
public class Company {
    @Id @GeneratedValue(strategy = GenerationType.AUTO) long id;
    String name;
    @Enumerated(ORDINAL) Sector sector;
    @ManyToMany
    @JoinTable(name = "company_employee", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "company_id"))
    Set<Employee> employees;

    public enum Sector {
        Services, Manufacturing;
    }
}
