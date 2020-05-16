package com.github.chrisgleissner.microservice.openfeign.security;

import org.immutables.value.Value;

@Value.Immutable
public interface UserCredentials {
    String getUsername();
    String getPassword();
}
