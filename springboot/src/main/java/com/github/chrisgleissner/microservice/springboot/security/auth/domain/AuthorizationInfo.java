package com.github.chrisgleissner.microservice.springboot.security.auth.domain;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Set;

@Value @NoArgsConstructor(force = true) @RequiredArgsConstructor
public class AuthorizationInfo {
    String username;
    Set<String> roles;
}