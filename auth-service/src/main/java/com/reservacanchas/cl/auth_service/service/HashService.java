package com.reservacanchas.cl.auth_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;

@Service
public class HashService {

    private static final Logger logger = LoggerFactory.getLogger(HashService.class);

    public String sha1(String input) {

        logger.debug("Iniciando proceso de hash SHA-1");

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            byte[] digest = md.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            String hash = toHex(digest);

            logger.debug("Hash SHA-1 generado correctamente");

            return hash;

        } catch (Exception e) {
            logger.error("Error al generar hash SHA-1", e);
            throw new RuntimeException("No se pudo encriptar la contraseña", e);
        }
    }

    private String toHex(byte[] bytes) {

        logger.debug("Convirtiendo bytes de hash a formato hexadecimal");

        StringBuilder sb = new StringBuilder(bytes.length * 2);

        for (byte b : bytes) {
            sb.append(String.format("%02x", b & 0xff));
        }

        logger.debug("Conversión hexadecimal finalizada correctamente");

        return sb.toString();
    }
}