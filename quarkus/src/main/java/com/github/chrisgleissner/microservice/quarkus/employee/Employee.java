package com.github.chrisgleissner.microservice.quarkus.employee;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Entity
public class Employee extends PanacheEntityBase {
    @Id
    @Column( name = "id" )
    public Long id;
    public String firstname;
    public String lastname;
    public LocalDate birthday;

    public static Optional<Employee> findById(Long id){
        return find("id", id).firstResultOptional();
    }

    public static List<Employee> findByLastName(String lastName) {
        return find("select u from Employee u where lower(u.lastname) = lower(?1)", lastName).list();
    }
}
