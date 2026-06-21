package com.reservacanchas.cl.auth_service.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getKey() {
        logger.debug("Generando clave secreta para firma JWT");
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String email, String role) {

        logger.info("Iniciando generación de token JWT para email: {} con rol: {}", email, role);

        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + 1000 * 60 * 60);

        String token = Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(getKey(), Jwts.SIG.HS384)
                .compact();

        logger.info("Token JWT generado correctamente para email: {}. Expira en: {}", email, expiracion);

        return token;
    }

    public String getEmailFromToken(String token) {

        logger.info("Iniciando extracción de email desde token JWT");

        if (token == null || token.isBlank()) {
            logger.warn("No se pudo extraer email: token JWT nulo o vacío");
            return null;
        }

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

        try {
            String email = Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload()
                    .getSubject();

            logger.info("Email extraído correctamente desde token JWT: {}", email);

            return email;

        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Token JWT inválido al intentar extraer email: {}", e.getMessage());
            return null;
        }
    }

    public boolean isValid(String token) {

        logger.info("Iniciando validación de token JWT");

        if (token == null || token.isBlank()) {
            logger.warn("Token JWT inválido: token nulo o vacío");
            return false;
        }

        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

        try {
            Jwts.parser()
                    .verifyWith(getKey())
                    .build()
                    .parseSignedClaims(jwt);

            logger.info("Token JWT validado correctamente");

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Token JWT inválido: {}", e.getMessage());
            return false;
        }
    }
}