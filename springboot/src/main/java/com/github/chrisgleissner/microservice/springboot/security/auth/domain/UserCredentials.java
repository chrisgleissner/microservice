package com.github.chrisgleissner.microservice.springboot.security.auth.domain;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value @NoArgsConstructor(force = true) @RequiredArgsConstructor
public class UserCredentials {
    String username;
    String password;
}
