package com.reservacanchas.cl.resena_service.config;

import com.reservacanchas.cl.resena_service.model.Resena;
import com.reservacanchas.cl.resena_service.repository.ResenaRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoaderConfig {

    @Bean
    CommandLineRunner cargarResenas(
            ResenaRepository resenaRepository) {

        return args -> {

            if (resenaRepository.count() >= 10) {
                return;
            }

            resenaRepository.save(new Resena(
                    null, 1L, 1L, 1L,
                    5,
                    "Excelente cancha, muy bien mantenida",
                    "2026-06-01"
            ));

            resenaRepository.save(new Resena(
                    null, 2L, 2L, 2L,
                    4,
                    "Muy buena experiencia",
                    "2026-06-02"
            ));

            resenaRepository.save(new Resena(
                    null, 3L, 3L, 3L,
                    5,
                    "Instalaciones impecables",
                    "2026-06-03"
            ));

            resenaRepository.save(new Resena(
                    null, 4L, 4L, 4L,
                    3,
                    "Buen servicio pero faltaba iluminación",
                    "2026-06-04"
            ));

            resenaRepository.save(new Resena(
                    null, 5L, 5L, 5L,
                    4,
                    "Cancha en excelente estado",
                    "2026-06-05"
            ));

            resenaRepository.save(new Resena(
                    null, 6L, 6L, 6L,
                    5,
                    "La mejor cancha de la zona",
                    "2026-06-06"
            ));

            resenaRepository.save(new Resena(
                    null, 7L, 7L, 7L,
                    4,
                    "Buena atención del personal",
                    "2026-06-07"
            ));

            resenaRepository.save(new Resena(
                    null, 8L, 8L, 8L,
                    5,
                    "Reserva rápida y sencilla",
                    "2026-06-08"
            ));

            resenaRepository.save(new Resena(
                    null, 9L, 9L, 9L,
                    4,
                    "Volvería a reservar sin problemas",
                    "2026-06-09"
            ));

            System.out.println("Reseñas cargadas correctamente");
        };
    }
}