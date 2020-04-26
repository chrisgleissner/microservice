package com.github.chrisgleissner.microservice.springboot.rest.security.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static lombok.AccessLevel.PROTECTED;

@Entity @Table(name = "user")
@Cacheable @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Value @NoArgsConstructor(access = PROTECTED, force = true) @AllArgsConstructor @Builder(toBuilder = true)
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    @NotEmpty String username;
    @NotEmpty String password;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "role_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<AppRole> roles;

    public AppUser(@NotEmpty String username, @NotEmpty String password, List<String> roles) {
        this(null, username, password, roles.stream().map(AppRole::new).collect(toList()));
    }
}
