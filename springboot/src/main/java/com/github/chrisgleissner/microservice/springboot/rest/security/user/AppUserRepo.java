package com.github.chrisgleissner.microservice.springboot.rest.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.chrisgleissner.microservice.springboot.rest.security.user.Roles.ADMIN_ROLE;
import static com.github.chrisgleissner.microservice.springboot.rest.security.user.Roles.USER_ROLE;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component @RequiredArgsConstructor
public class AppUserRepo {
    public static final AppUser NORMAL_APP_USER = new AppUser(1, "user", "secret", USER_ROLE);
    public static final AppUser ADMIN_APP_USER = new AppUser(2, "admin", "secret2", ADMIN_ROLE);
    private final Map<String, AppUser> appUsersByUsername;

    @Autowired
    AppUserRepo(PasswordEncoder passwordEncoder) {
        this.appUsersByUsername = List.of(NORMAL_APP_USER, ADMIN_APP_USER).stream()
                .map(u -> u.toBuilder().password(passwordEncoder.encode(u.getPassword())).build())
                .collect(toMap(AppUser::getUsername, identity()));
    }

    // TODO Load from DB
    public Optional<AppUser> findByUsername(String username) {
        return Optional.ofNullable(appUsersByUsername.get(username));
    }
}
