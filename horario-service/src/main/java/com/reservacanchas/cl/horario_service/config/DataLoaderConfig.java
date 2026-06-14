package com.reservacanchas.cl.horario_service.config;

import com.reservacanchas.cl.horario_service.model.Horario;
import com.reservacanchas.cl.horario_service.repository.HorarioRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoaderConfig {

    @Bean
    CommandLineRunner cargarHorarios(
            HorarioRepository horarioRepository) {

        return args -> {
            if (horarioRepository.count() >= 10) {
                return;
            }

            horarioRepository.save(new Horario(
                    null, 1L, "LUNES", "08:00", "10:00", "ACTIVO"));

            horarioRepository.save(new Horario(
                    null, 2L, "MARTES", "09:00", "11:00", "ACTIVO"));

            horarioRepository.save(new Horario(
                    null, 3L, "MIERCOLES", "10:00", "12:00", "ACTIVO"));

            horarioRepository.save(new Horario(
                    null, 4L, "JUEVES", "11:00", "13:00", "ACTIVO"));

            horarioRepository.save(new Horario(
                    null, 5L, "VIERNES", "12:00", "14:00", "ACTIVO"));

            horarioRepository.save(new Horario(
                    null, 6L, "SABADO", "08:00", "10:00", "ACTIVO"));

            horarioRepository.save(new Horario(
                    null, 7L, "DOMINGO", "09:00", "11:00", "ACTIVO"));

            horarioRepository.save(new Horario(
                    null, 8L, "LUNES", "14:00", "16:00", "ACTIVO"));

            horarioRepository.save(new Horario(
                    null, 9L, "MIERCOLES", "16:00", "18:00", "ACTIVO"));

            horarioRepository.save(new Horario(
                    null, 10L, "VIERNES", "18:00", "20:00", "ACTIVO"));

            System.out.println("Horarios cargados correctamente");
        };
    }
}