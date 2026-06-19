package com.reservacanchas.cl.auth_service.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email, String role) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + 1000 * 60 * 60);

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(getKey(), Jwts.SIG.HS384)
                .compact();
    }

    public String getEmailFromToken(String token) {
        if (token == null || token.isBlank()) return null;

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

        try {
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public boolean isValid(String token) {
        if (token == null || token.isBlank()) return false;

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

        try {
            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}