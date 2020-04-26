package com.github.chrisgleissner.microservice.springboot.rest.security.user;

import com.github.chrisgleissner.microservice.springboot.rest.security.user.domain.AppUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AppUserRepo extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
