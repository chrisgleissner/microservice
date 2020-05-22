package com.github.chrisgleissner.microservice.openfeign.java7.employee;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;
import org.joda.time.LocalDate;

@Value.Immutable @JsonDeserialize(as=ImmutableEmployee.class)
public interface Employee {
    Long getId();
    String getFirstname();
    String getLastname();
    LocalDate getBirthday();
}
