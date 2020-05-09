package com.github.chrisgleissner.microservice.springboot.security.auth.user;

import com.github.chrisgleissner.microservice.springboot.security.auth.user.repo.AppRole;
import com.github.chrisgleissner.microservice.springboot.security.auth.user.repo.AppUser;

import java.util.List;

public class UserConstants {
    public static final String ADMIN_USER_NAME = "admin";
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    public static final AppUser NORMAL_APP_USER = new AppUser(1L, "user", "secret", List.of(new AppRole(1L, USER_ROLE)));
    public static final AppUser ADMIN_APP_USER = new AppUser(2L, ADMIN_USER_NAME, "secret2", List.of(new AppRole(2L, ADMIN_ROLE)));
}
