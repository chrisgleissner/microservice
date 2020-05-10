package com.github.chrisgleissner.microservice.springboot.security.auth;

import com.github.chrisgleissner.microservice.springboot.security.auth.domain.AuthorizationInfo;
import com.github.chrisgleissner.microservice.springboot.security.auth.user.repo.AppRole;
import com.github.chrisgleissner.microservice.springboot.security.auth.user.repo.AppUser;
import com.github.chrisgleissner.microservice.springboot.security.auth.user.repo.AppUserRepo;
import com.github.chrisgleissner.microservice.springboot.security.auth.jwt.JwtConfig;
import com.github.chrisgleissner.microservice.springboot.security.auth.jwt.JwtManager;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.toCollection;

@Service @RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtManager jwtManager;
    private final JwtConfig jwtConfig;
    private final AppUserRepo appUserRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override public String getJwt(String username, String password) throws AuthenticationException {
        return appUserRepo.findByUsername(username)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .map(this::createJwt)
                .orElseThrow(() -> new AuthenticationException("Invalid password for user " + username));
    }

    private String createJwt(AppUser user) {
        if (jwtConfig.isEncodeRolesInJwt()) {
            Set<String> roles = user.getRoles().stream().map(AppRole::getName).collect(toCollection(TreeSet::new));
            return jwtManager.createJwt(user.getUsername(), roles);
        } else {
            return jwtManager.createJwt(user.getUsername());
        }
    }

    @Override public AuthorizationInfo getAuthorizationInfo(String jwt) {
        val claims = jwtManager.getClaims(jwt);
        val username = claims.getSubject();
        return new AuthorizationInfo(username, getRoles(claims, username));
    }

    private Set<String> getRoles(Claims claims, String username) {
        Optional<Collection<String>> maybeRolesFromJwt = jwtManager.getRoles(claims);
        if (jwtConfig.isEncodeRolesInJwt() && maybeRolesFromJwt.isPresent())
            return Set.copyOf(maybeRolesFromJwt.get());
        else
            return loadRolesFromRepo(username);
    }

    private Set<String> loadRolesFromRepo(String username) {
        return appUserRepo.findByUsername(username).map(au -> au.getRoles().stream()
                .map(AppRole::getName)
                .sorted()
                .collect(toCollection(TreeSet::new)))
                .orElseThrow(() -> new RuntimeException("Can't find user " + username));
    }
}
