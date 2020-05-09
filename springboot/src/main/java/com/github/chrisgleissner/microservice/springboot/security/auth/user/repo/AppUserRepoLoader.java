package com.github.chrisgleissner.microservice.springboot.security.auth.user.repo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component @RequiredArgsConstructor @Slf4j
public class AppUserRepoLoader implements ApplicationRunner {
    private final AppUserRepo appUserRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override public void run(ApplicationArguments args) {
        log.info("Creating users");
        appUserRepo.save(new AppUser("user", passwordEncoder.encode("secret"), List.of("USER")));
        appUserRepo.save(new AppUser("user1", passwordEncoder.encode("secret"), List.of("USER")));
        appUserRepo.save(new AppUser("user2", passwordEncoder.encode("secret"), List.of("USER")));
        appUserRepo.save(new AppUser("user3", passwordEncoder.encode("secret"), List.of("USER")));
        appUserRepo.save(new AppUser("admin", passwordEncoder.encode("secret2"), List.of("ADMIN")));
    }
}
