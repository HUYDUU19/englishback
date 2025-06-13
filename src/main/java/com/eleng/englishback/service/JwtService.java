package com.eleng.englishback.service;

import org.springframework.stereotype.Service;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
// Temporarily commented out due to User class compilation issues
// import com.eleng.englishback.domain.User;
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

    // Temporarily modified to work without User entity due to compilation issues
    public String generateToken(String username, String role) {
        Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Original method commented out due to User class compilation issues
    // public String generateToken(User user) {
    // Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
    // SignatureAlgorithm.HS256.getJcaName());
    // return Jwts.builder()
    // .setSubject(user.getUsername())
    // .claim("role", user.getRole().name())
    // .setIssuedAt(new Date())
    // .setExpiration(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
    // .signWith(key, SignatureAlgorithm.HS256)
    // .compact();
    // }

    public String extractUsername(String token) {
        Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    } // Thêm phương thức isTokenValid

    public boolean isTokenValid(String token) {
        try {
            // Kiểm tra token có hết hạn chưa
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }
}
