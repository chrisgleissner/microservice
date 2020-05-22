package com.github.chrisgleissner.microservice.openfeign.java11.security;

import lombok.Value;

@Value
public class UserCredentials {
    String username;
    String password;
}
