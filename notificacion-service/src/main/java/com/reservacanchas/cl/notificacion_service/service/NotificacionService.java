package com.reservacanchas.cl.notificacion_service.service;

import com.reservacanchas.cl.notificacion_service.dto.NotificacionDTO;
import com.reservacanchas.cl.notificacion_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.notificacion_service.model.Notificacion;
import com.reservacanchas.cl.notificacion_service.repository.NotificacionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NotificacionService {

    private static final Logger logger =
            LoggerFactory.getLogger(NotificacionService.class);

    private final NotificacionRepository notificacionRepository;
    private final RestTemplate restTemplate;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
        this.restTemplate = new RestTemplate();
    }

    public Notificacion guardar(NotificacionDTO notificacionDTO) {

        logger.info(
                "Iniciando creación de notificación para usuario {} y reserva {}",
                notificacionDTO.getIdUsuario(),
                notificacionDTO.getIdReserva()
        );

        Boolean usuarioExiste = restTemplate.getForObject(
                "http://localhost:7091/usuarios/"
                        + notificacionDTO.getIdUsuario()
                        + "/exists",
                Boolean.class
        );

        if (usuarioExiste == null || !usuarioExiste) {

            logger.warn(
                    "No se pudo crear la notificación. Usuario {} no existe",
                    notificacionDTO.getIdUsuario()
            );

            throw new ResourceNotFoundException("El usuario no existe");
        }

        Boolean reservaExiste = restTemplate.getForObject(
                "http://localhost:7093/reservas/"
                        + notificacionDTO.getIdReserva()
                        + "/exists",
                Boolean.class
        );

        if (reservaExiste == null || !reservaExiste) {

            logger.warn(
                    "No se pudo crear la notificación. Reserva {} no existe",
                    notificacionDTO.getIdReserva()
            );

            throw new ResourceNotFoundException("La reserva no existe");
        }

        Notificacion notificacion = new Notificacion();

        notificacion.setIdUsuario(notificacionDTO.getIdUsuario());
        notificacion.setIdReserva(notificacionDTO.getIdReserva());
        notificacion.setMensaje(notificacionDTO.getMensaje());
        notificacion.setTipoNotificacion(
                notificacionDTO.getTipoNotificacion());
        notificacion.setFechaEnvio(notificacionDTO.getFechaEnvio());

        // Estado automático del negocio
        notificacion.setEstado("ENVIADA");

        Notificacion notificacionGuardada =
                notificacionRepository.save(notificacion);

        logger.info(
                "Notificación creada correctamente con ID {}",
                notificacionGuardada.getId()
        );

        return notificacionGuardada;
    }

    public List<Notificacion> listar() {

        logger.info("Listando todas las notificaciones");

        return notificacionRepository.findAll();
    }

    public Notificacion buscarPorId(Long id) {

        logger.info("Buscando notificación con ID {}", id);

        return notificacionRepository.findById(id)
                .orElseThrow(() -> {

                    logger.warn(
                            "Notificación con ID {} no encontrada",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Notificación no encontrada");
                });
    }

    public Notificacion actualizar(
            Long id,
            NotificacionDTO notificacionDTO) {

        logger.info(
                "Actualizando notificación con ID {}",
                id
        );

        Notificacion notificacion = buscarPorId(id);

        notificacion.setIdUsuario(notificacionDTO.getIdUsuario());
        notificacion.setIdReserva(notificacionDTO.getIdReserva());
        notificacion.setMensaje(notificacionDTO.getMensaje());
        notificacion.setTipoNotificacion(
                notificacionDTO.getTipoNotificacion());
        notificacion.setFechaEnvio(notificacionDTO.getFechaEnvio());
        notificacion.setEstado(notificacionDTO.getEstado());

        Notificacion notificacionActualizada =
                notificacionRepository.save(notificacion);

        logger.info(
                "Notificación con ID {} actualizada correctamente",
                id
        );

        return notificacionActualizada;
    }

    public void eliminar(Long id) {

        logger.info(
                "Eliminando notificación con ID {}",
                id
        );

        Notificacion notificacion = buscarPorId(id);

        notificacionRepository.delete(notificacion);

        logger.info(
                "Notificación con ID {} eliminada correctamente",
                id
        );
    }

    public boolean existePorId(Long id) {

        logger.info(
                "Verificando existencia de notificación con ID {}",
                id
        );

        return notificacionRepository.existsById(id);
    }
}