package com.reservacanchas.cl.auth_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;

@Service
public class HashService {

    private static final Logger logger =
            LoggerFactory.getLogger(HashService.class);

    public String sha1(String input) {

        logger.debug("Generando hash SHA-1");

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-1");

            byte[] digest = md.digest(
                    input.getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );

            logger.debug("Hash SHA-1 generado correctamente");

            return toHex(digest);

        } catch (Exception e) {

            logger.error("Error al generar hash SHA-1", e);

            throw new RuntimeException(
                    "No se pudo encriptar la contraseña",
                    e
            );
        }
    }

    private String toHex(byte[] bytes) {

        StringBuilder sb = new StringBuilder(bytes.length * 2);

        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }

        return sb.toString();
    }
}