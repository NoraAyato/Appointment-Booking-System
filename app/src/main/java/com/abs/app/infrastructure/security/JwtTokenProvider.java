package com.abs.app.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {
    private final Key accessTokenKey;
    private final Key resetPasswordTokenKey;
    private final long expiration;
    private final long refreshTokenExpiration;
    private final long resetPasswordTokenExpiration;

    public JwtTokenProvider(
            @Value("${security.jwt.secret}") String jwtSecret,
            @Value("${security.jwt.reset-secret}") String resetPasswordSecret,
            @Value("${security.jwt.expiration}") long expiration,
            @Value("${security.jwt.refresh-expiration}") long refreshTokenExpiration,
            @Value("${security.jwt.reset-expiration}") long resetPasswordTokenExpiration) {
        this.accessTokenKey = buildHmacKey(jwtSecret, "security.jwt.secret");
        this.resetPasswordTokenKey = buildHmacKey(resetPasswordSecret, "security.jwt.reset-secret");
        this.expiration = expiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.resetPasswordTokenExpiration = resetPasswordTokenExpiration;
    }

    private Key buildHmacKey(String rawSecret, String propertyName) {
        String secret = rawSecret == null ? "" : rawSecret.trim();
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        if (keyBytes.length < 32) {
            throw new IllegalArgumentException(propertyName + " must be at least 32 bytes for HS256");
        }

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String userId, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(accessTokenKey)
                .compact();
    }

    public String getUserIdFromResetToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(resetPasswordTokenKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String generateResetPasswordToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + resetPasswordTokenExpiration);

        return Jwts.builder()
                .setSubject(userId)
                .claim("type", "reset_password")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(resetPasswordTokenKey)
                .compact();
    }

    public String generateRefreshToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setId(UUID.randomUUID().toString())
                .signWith(accessTokenKey)
                .compact();
    }

    public String getRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(accessTokenKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(accessTokenKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(accessTokenKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
