package com.github.chrisgleissner.microservice.springboot.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import static com.github.chrisgleissner.microservice.springboot.security.jwt.JwtConfig.AUTHORITIES_CLAIM;

@Component @RequiredArgsConstructor
public class JwtManager {
    private final JwtConfig jwtConfig;

    public String createJwt(String username, Set<String> roles) {
        final long millisSinceEpoch = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(username)
                .claim(AUTHORITIES_CLAIM, List.copyOf(roles))
                .setIssuedAt(new Date(millisSinceEpoch))
                .setExpiration(new Date(millisSinceEpoch + jwtConfig.getExpiration() * 1000))  // in milliseconds
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();
    }

    public Claims getClaims(String jwt) {
        return Jwts.parser().setSigningKey(jwtConfig.getSecret().getBytes()).parseClaimsJws(jwt).getBody();
    }

    public static String getUsername(Claims claims) {
        return claims.getSubject();
    }
}
