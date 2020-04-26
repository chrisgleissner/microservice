package com.github.chrisgleissner.microservice.springboot.rest.security.user;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.chrisgleissner.microservice.springboot.rest.security.user.Roles.ADMIN_ROLE;
import static com.github.chrisgleissner.microservice.springboot.rest.security.user.Roles.USER_ROLE;

public class AppUserRepo {
    public static final AppUser NORMAL_APP_USER = new AppUser(1, "user", "secret", USER_ROLE);
    public static final AppUser ADMIN_APP_USER = new AppUser(2, "admin", "secret2", ADMIN_ROLE);

    // TODO Load from DB
    public static List<AppUser> users(PasswordEncoder encoder) {
        return List.of(NORMAL_APP_USER, ADMIN_APP_USER).stream().map(u -> u.toBuilder().password(encoder.encode(u.getPassword())).build())
                .collect(Collectors.toList());
    }
}
