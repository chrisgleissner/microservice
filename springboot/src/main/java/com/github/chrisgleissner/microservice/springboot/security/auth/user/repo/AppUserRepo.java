package com.github.chrisgleissner.microservice.springboot.security.auth.user.repo;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AppUserRepo extends CrudRepository<AppUser, Long> {
    @Cacheable("appuser")
    Optional<AppUser> findByUsername(String username);
}
