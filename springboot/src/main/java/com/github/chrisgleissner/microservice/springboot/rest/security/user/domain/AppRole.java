package com.github.chrisgleissner.microservice.springboot.rest.security.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import static lombok.AccessLevel.PROTECTED;

@Entity @Table(name = "role")
@Cacheable @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Value @NoArgsConstructor(access = PROTECTED, force = true) @AllArgsConstructor @Builder(toBuilder = true)
public class AppRole {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @NotEmpty String name;

    public AppRole(@NotEmpty String name) {
        this(null, name);
    }
}
