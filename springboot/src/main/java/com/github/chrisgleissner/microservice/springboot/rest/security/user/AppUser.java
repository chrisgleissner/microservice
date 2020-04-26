package com.github.chrisgleissner.microservice.springboot.rest.security.user;

import lombok.Builder;
import lombok.Value;

@Value @Builder(toBuilder = true)
public class AppUser {
    Integer id;
    String username;
    String password;
    String role;
}
