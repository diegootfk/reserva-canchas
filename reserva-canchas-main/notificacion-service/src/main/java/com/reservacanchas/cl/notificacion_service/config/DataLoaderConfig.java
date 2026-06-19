package com.reservacanchas.cl.notificacion_service.config;

import com.reservacanchas.cl.notificacion_service.model.Notificacion;
import com.reservacanchas.cl.notificacion_service.repository.NotificacionRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoaderConfig {

    @Bean
    CommandLineRunner cargarNotificaciones(
            NotificacionRepository notificacionRepository) {

        return args -> {

            if (notificacionRepository.count() >= 10) {
                return;
            }

            notificacionRepository.save(new Notificacion(
                    null, 1L, 1L,
                    "Reserva confirmada correctamente",
                    "EMAIL",
                    "2026-06-01",
                    "ENVIADA"
            ));

            notificacionRepository.save(new Notificacion(
                    null, 2L, 2L,
                    "Recordatorio de reserva para mañana",
                    "EMAIL",
                    "2026-06-02",
                    "ENVIADA"
            ));

            notificacionRepository.save(new Notificacion(
                    null, 3L, 3L,
                    "Pago recibido exitosamente",
                    "SMS",
                    "2026-06-03",
                    "ENVIADA"
            ));

            notificacionRepository.save(new Notificacion(
                    null, 4L, 4L,
                    "Reserva cancelada",
                    "EMAIL",
                    "2026-06-04",
                    "ENVIADA"
            ));

            notificacionRepository.save(new Notificacion(
                    null, 5L, 5L,
                    "Cancha disponible nuevamente",
                    "PUSH",
                    "2026-06-05",
                    "ENVIADA"
            ));

            notificacionRepository.save(new Notificacion(
                    null, 6L, 6L,
                    "Cambio de horario confirmado",
                    "EMAIL",
                    "2026-06-06",
                    "ENVIADA"
            ));

            notificacionRepository.save(new Notificacion(
                    null, 7L, 7L,
                    "Reserva próxima a vencer",
                    "SMS",
                    "2026-06-07",
                    "ENVIADA"
            ));

            notificacionRepository.save(new Notificacion(
                    null, 8L, 8L,
                    "Nueva promoción disponible",
                    "PUSH",
                    "2026-06-08",
                    "ENVIADA"
            ));

            notificacionRepository.save(new Notificacion(
                    null, 9L, 9L,
                    "Mantenimiento programado en la cancha",
                    "EMAIL",
                    "2026-06-09",
                    "ENVIADA"
            ));

            System.out.println("Notificaciones cargadas correctamente");
        };
    }
}