package com.github.chrisgleissner.microservice.springboot.rest.security.jwt.create;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.github.chrisgleissner.microservice.springboot.rest.security.jwt.create.UserConstants.*;

@Service @RequiredArgsConstructor @Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Invoked loadUserByUsername({})", username);
        // TODO Load from DB
        final List<AppUser> users = List.of(
                new AppUser(1, USER_NAME, passwordEncoder.encode(USER_PASSWORD), USER_ROLE),
                new AppUser(2, ADMIN_NAME, passwordEncoder.encode(ADMIN_PASSWORD), ADMIN_ROLE)
        );
        for (AppUser appUser : users) {
            if (appUser.getUsername().equals(username)) {
                // Spring requires "ROLE_" prefix for all roles, e.g. "ROLE_ADMIN" means hasRole("ADMIN") passes
                List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + appUser.getRole());
                return new User(appUser.getUsername(), appUser.getPassword(), grantedAuthorities);
            }
        }
        throw new UsernameNotFoundException("Username: " + username + " not found");
    }

    @Value
    private static class AppUser {
        Integer id;
        String username;
        String password;
        String role;
    }
}