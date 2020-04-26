package com.github.chrisgleissner.microservice.springboot.rest.security.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service @RequiredArgsConstructor @Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AppUserRepo appUserRepo;

    // Spring requires "ROLE_" prefix for all roles, e.g. "ROLE_ADMIN" means hasRole("ADMIN") passes
    private static String springRoleName(String roleName) {
        return "ROLE_" + roleName;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepo.findByUsername(username)
                .map(u -> new User(u.getUsername(), u.getPassword(), u.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority(springRoleName(r.getName()))).collect(toList())))
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
    }
}