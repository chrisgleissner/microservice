package com.github.chrisgleissner.microservice.springboot.security.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.github.chrisgleissner.microservice.springboot.security.auth.jwt.JwtUtil.AUTHORITIES_CLAIM;

@Component @RequiredArgsConstructor
public class JwtManager {
    private final JwtConfig jwtConfig;

    /**
     * Create JWT with roles.
     */
    public String createJwt(String username, Set<String> roles) {
        final long millisSinceEpoch = System.currentTimeMillis();
        JwtBuilder jwtBuilder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(millisSinceEpoch))
                .setExpiration(new Date(millisSinceEpoch + jwtConfig.getExpiration() * 1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes());
        Optional.ofNullable(roles).ifPresent(r -> jwtBuilder.claim(AUTHORITIES_CLAIM, List.copyOf(roles)));
        return jwtBuilder.compact();
    }

    public Optional<Collection<String>> getRoles(Claims claims) {
        List<String> roles = (List<String>) claims.get(AUTHORITIES_CLAIM);
        return Optional.ofNullable(roles);
    }


    /**
     * Create JWT without roles which are always loaded from the persistent store. This means roles can be changed
     * during the lifetime of the JWT.
     */
    public String createJwt(String username) {
        return createJwt(username, null);
    }

    public Claims getClaims(String jwt) {
        return Jwts.parser().setSigningKey(jwtConfig.getSecret().getBytes()).parseClaimsJws(jwt).getBody();
    }
}
