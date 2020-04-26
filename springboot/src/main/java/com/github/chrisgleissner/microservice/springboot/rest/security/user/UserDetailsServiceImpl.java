package com.github.chrisgleissner.microservice.springboot.rest.security.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor @Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (AppUser appUser : AppUserRepo.users(passwordEncoder)) {
            if (appUser.getUsername().equals(username)) {
                return new User(appUser.getUsername(), appUser.getPassword(),
                        AuthorityUtils.commaSeparatedStringToAuthorityList(springRoleName(appUser.getRole())));
            }
        }
        throw new UsernameNotFoundException("Username: " + username + " not found");
    }

    // Spring requires "ROLE_" prefix for all roles, e.g. "ROLE_ADMIN" means hasRole("ADMIN") passes
    private static String springRoleName(String roleName) {
        return "ROLE_" + roleName;
    }
}