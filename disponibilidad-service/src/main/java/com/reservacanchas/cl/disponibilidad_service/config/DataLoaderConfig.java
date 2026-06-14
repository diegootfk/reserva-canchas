package com.reservacanchas.cl.disponibilidad_service.config;

import com.reservacanchas.cl.disponibilidad_service.model.Disponibilidad;
import com.reservacanchas.cl.disponibilidad_service.repository.DisponibilidadRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoaderConfig {

    @Bean
    CommandLineRunner cargarDisponibilidades(
            DisponibilidadRepository disponibilidadRepository) {

        return args -> {

            // Si ya existen 10 o más registros no carga nada
            if (disponibilidadRepository.count() >= 10) {
                return;
            }

            disponibilidadRepository.save(new Disponibilidad(
                    null, 1L, "2026-06-20", "08:00", "09:00", "DISPONIBLE"));

            disponibilidadRepository.save(new Disponibilidad(
                    null, 2L, "2026-06-20", "09:00", "10:00", "DISPONIBLE"));

            disponibilidadRepository.save(new Disponibilidad(
                    null, 3L, "2026-06-20", "10:00", "11:00", "RESERVADA"));

            disponibilidadRepository.save(new Disponibilidad(
                    null, 4L, "2026-06-21", "11:00", "12:00", "DISPONIBLE"));

            disponibilidadRepository.save(new Disponibilidad(
                    null, 5L, "2026-06-21", "12:00", "13:00", "MANTENIMIENTO"));

            disponibilidadRepository.save(new Disponibilidad(
                    null, 6L, "2026-06-22", "13:00", "14:00", "DISPONIBLE"));

            disponibilidadRepository.save(new Disponibilidad(
                    null, 7L, "2026-06-22", "14:00", "15:00", "RESERVADA"));

            disponibilidadRepository.save(new Disponibilidad(
                    null, 8L, "2026-06-23", "15:00", "16:00", "DISPONIBLE"));

            disponibilidadRepository.save(new Disponibilidad(
                    null, 9L, "2026-06-23", "16:00", "17:00", "DISPONIBLE"));

            disponibilidadRepository.save(new Disponibilidad(
                    null, 10L, "2026-06-24", "17:00", "18:00", "MANTENIMIENTO"));

            System.out.println("Disponibilidades cargadas correctamente");
        };
    }
}