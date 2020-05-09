package com.github.chrisgleissner.microservice.springboot.security.auth;

import com.github.chrisgleissner.microservice.springboot.security.auth.domain.AuthorizationInfo;
import com.github.chrisgleissner.microservice.springboot.security.auth.user.repo.AppRole;
import com.github.chrisgleissner.microservice.springboot.security.auth.user.repo.AppUserRepo;
import com.github.chrisgleissner.microservice.springboot.security.jwt.JwtManager;
import com.github.chrisgleissner.microservice.springboot.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.toCollection;

@Service @RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtManager jwtManager;
    private final AppUserRepo appUserRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override public String getJwt(String username, String password) throws AuthenticationException {
        return appUserRepo.findByUsername(username)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .map(u -> jwtManager.createJwt(u.getUsername(), u.getRoles().stream()
                        .map(AppRole::getName)
                        // Spring requires "ROLE_" prefix for all roles, e.g. "ROLE_ADMIN" means hasRole("ADMIN") passes
                        .map(n -> "ROLE_" + n)
                        .collect(toCollection(TreeSet::new))))
                .orElseThrow(() -> new AuthenticationException("Invalid password for user " + username));
    }

    @Override public AuthorizationInfo getAuthorizationInfo(String jwt) {
        val username = JwtUtil.getUsername(jwtManager.getClaims(jwt));
        return new AuthorizationInfo(username, getRoles(username));
    }

    private Set<String> getRoles(String username) {
        return appUserRepo.findByUsername(username).map(au -> au.getRoles().stream()
                .map(AppRole::getName)
                .collect(toCollection(TreeSet::new)))
                .orElseThrow(() -> new RuntimeException("Can't find user " + username));
    }
}
