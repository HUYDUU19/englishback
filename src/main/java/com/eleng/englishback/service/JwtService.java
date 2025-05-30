package com.eleng.englishback.service;

import org.springframework.stereotype.Service;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import com.eleng.englishback.domain.User;
import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import io.jsonwebtoken.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Data
@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String secretKey;

    public String generateToken(User user) {
        Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
