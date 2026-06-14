package com.reservacanchas.cl.mantenimiento_service.config;

import com.reservacanchas.cl.mantenimiento_service.model.Mantenimiento;
import com.reservacanchas.cl.mantenimiento_service.repository.MantenimientoRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoaderConfig {

    @Bean
    CommandLineRunner cargarMantenimientos(
            MantenimientoRepository mantenimientoRepository) {

        return args -> {

            if (mantenimientoRepository.count() >= 10) {
                return;
            }

            mantenimientoRepository.save(new Mantenimiento(
                    null, 1L,
                    "2026-06-01",
                    "2026-06-02",
                    "Cambio de malla perimetral",
                    "FINALIZADO"
            ));

            mantenimientoRepository.save(new Mantenimiento(
                    null, 2L,
                    "2026-06-03",
                    "2026-06-04",
                    "Pintura de líneas",
                    "FINALIZADO"
            ));

            mantenimientoRepository.save(new Mantenimiento(
                    null, 3L,
                    "2026-06-05",
                    "2026-06-06",
                    "Reparación de iluminación",
                    "FINALIZADO"
            ));

            mantenimientoRepository.save(new Mantenimiento(
                    null, 4L,
                    "2026-06-07",
                    "2026-06-08",
                    "Mantención de camarines",
                    "FINALIZADO"
            ));

            mantenimientoRepository.save(new Mantenimiento(
                    null, 5L,
                    "2026-06-09",
                    "2026-06-10",
                    "Cambio de césped sintético",
                    "EN_PROCESO"
            ));

            mantenimientoRepository.save(new Mantenimiento(
                    null, 6L,
                    "2026-06-11",
                    "2026-06-12",
                    "Revisión de drenaje",
                    "PROGRAMADO"
            ));

            mantenimientoRepository.save(new Mantenimiento(
                    null, 7L,
                    "2026-06-13",
                    "2026-06-14",
                    "Cambio de arcos",
                    "PROGRAMADO"
            ));

            mantenimientoRepository.save(new Mantenimiento(
                    null, 8L,
                    "2026-06-15",
                    "2026-06-16",
                    "Reparación de cierre perimetral",
                    "PROGRAMADO"
            ));

            mantenimientoRepository.save(new Mantenimiento(
                    null, 9L,
                    "2026-06-17",
                    "2026-06-18",
                    "Limpieza profunda",
                    "PROGRAMADO"
            ));

            System.out.println("Mantenimientos cargados correctamente");
        };
    }
}