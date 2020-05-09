package com.github.chrisgleissner.microservice.springboot.security.auth.user;

import com.github.chrisgleissner.microservice.springboot.security.auth.user.repo.AppUserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service @RequiredArgsConstructor @Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AppUserRepo appUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepo.findByUsername(username)
                .map(u -> new User(u.getUsername(), u.getPassword(), u.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority(SpringRoleNameUtil.toSpringRoleName(r.getName()))).collect(toList())))
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + " not found"));
    }
}