package com.reservacanchas.cl.notificacion_service.service;

import com.reservacanchas.cl.notificacion_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.notificacion_service.model.Notificacion;
import com.reservacanchas.cl.notificacion_service.repository.NotificacionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NotificacionService {

    private static final Logger logger = LoggerFactory.getLogger(NotificacionService.class);

    private static final String USUARIO_SERVICE_URL = "http://localhost:7091/usuarios/";
    private static final String RESERVA_SERVICE_URL = "http://localhost:7093/reservas/";

    private final NotificacionRepository notificacionRepository;
    private final RestTemplate restTemplate;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
        this.restTemplate = new RestTemplate();
    }

    public Notificacion guardar(Notificacion notificacion) {

        logger.info(
                "Iniciando creación de notificación para usuario ID: {} y reserva ID: {}",
                notificacion.getIdUsuario(),
                notificacion.getIdReserva()
        );

        validarUsuarioExiste(notificacion.getIdUsuario());
        validarReservaExiste(notificacion.getIdReserva());

        notificacion.setEstado("ENVIADA");

        Notificacion notificacionGuardada = notificacionRepository.save(notificacion);

        logger.info("Notificación creada correctamente con ID: {}", notificacionGuardada.getId());

        return notificacionGuardada;
    }

    public List<Notificacion> listar() {

        logger.info("Iniciando búsqueda de todas las notificaciones");

        List<Notificacion> notificaciones = notificacionRepository.findAll();

        logger.info("Búsqueda finalizada. Total de notificaciones encontradas: {}", notificaciones.size());

        return notificaciones;
    }

    public Notificacion buscarPorId(Long id) {

        logger.info("Iniciando búsqueda de notificación con ID: {}", id);

        return notificacionRepository.findById(id)
                .map(notificacion -> {
                    logger.info("Notificación encontrada correctamente con ID: {}", id);
                    return notificacion;
                })
                .orElseThrow(() -> {
                    logger.warn("Notificación no encontrada con ID: {}", id);
                    return new ResourceNotFoundException("Notificación no encontrada");
                });
    }

    public Notificacion actualizar(Long id, Notificacion notificacionActualizada) {

        logger.info("Iniciando actualización de notificación con ID: {}", id);

        Notificacion notificacion = buscarPorId(id);

        validarUsuarioExiste(notificacionActualizada.getIdUsuario());
        validarReservaExiste(notificacionActualizada.getIdReserva());

        notificacion.setIdUsuario(notificacionActualizada.getIdUsuario());
        notificacion.setIdReserva(notificacionActualizada.getIdReserva());
        notificacion.setMensaje(notificacionActualizada.getMensaje());
        notificacion.setTipoNotificacion(notificacionActualizada.getTipoNotificacion());
        notificacion.setFechaEnvio(notificacionActualizada.getFechaEnvio());
        notificacion.setEstado(notificacionActualizada.getEstado());

        Notificacion notificacionGuardada = notificacionRepository.save(notificacion);

        logger.info("Notificación actualizada correctamente con ID: {}", id);

        return notificacionGuardada;
    }

    public void eliminar(Long id) {

        logger.warn("Iniciando eliminación de notificación con ID: {}", id);

        Notificacion notificacion = buscarPorId(id);

        notificacionRepository.delete(notificacion);

        logger.info("Notificación eliminada correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de notificación con ID: {}", id);

        boolean existe = notificacionRepository.existsById(id);

        logger.info("Resultado de existencia para notificación con ID {}: {}", id, existe);

        return existe;
    }

    private void validarUsuarioExiste(Long idUsuario) {

        logger.info("Validando existencia del usuario con ID: {}", idUsuario);

        try {
            Boolean usuarioExiste = restTemplate.getForObject(
                    USUARIO_SERVICE_URL + idUsuario + "/exists",
                    Boolean.class
            );

            if (usuarioExiste == null || !usuarioExiste) {
                logger.warn("Validación fallida: usuario con ID {} no existe", idUsuario);
                throw new ResourceNotFoundException("El usuario no existe");
            }

            logger.info("Usuario validado correctamente con ID: {}", idUsuario);

        } catch (RestClientException ex) {
            logger.error("Error remoto al validar usuario con ID: {}", idUsuario, ex);
            throw new ResourceNotFoundException("No se pudo validar la existencia del usuario");
        }
    }

    private void validarReservaExiste(Long idReserva) {

        logger.info("Validando existencia de la reserva con ID: {}", idReserva);

        try {
            Boolean reservaExiste = restTemplate.getForObject(
                    RESERVA_SERVICE_URL + idReserva + "/exists",
                    Boolean.class
            );

            if (reservaExiste == null || !reservaExiste) {
                logger.warn("Validación fallida: reserva con ID {} no existe", idReserva);
                throw new ResourceNotFoundException("La reserva no existe");
            }

            logger.info("Reserva validada correctamente con ID: {}", idReserva);

        } catch (RestClientException ex) {
            logger.error("Error remoto al validar reserva con ID: {}", idReserva, ex);
            throw new ResourceNotFoundException("No se pudo validar la existencia de la reserva");
        }
    }
}