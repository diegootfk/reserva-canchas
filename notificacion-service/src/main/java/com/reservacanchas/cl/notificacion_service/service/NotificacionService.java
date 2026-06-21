package com.reservacanchas.cl.notificacion_service.service;

import com.reservacanchas.cl.notificacion_service.dto.NotificacionDTO;
import com.reservacanchas.cl.notificacion_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.notificacion_service.model.Notificacion;
import com.reservacanchas.cl.notificacion_service.repository.NotificacionRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final RestTemplate restTemplate;

    public NotificacionService(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
        this.restTemplate = new RestTemplate();
    }

    public Notificacion guardar(NotificacionDTO notificacionDTO) {

        Boolean usuarioExiste = restTemplate.getForObject(
                "http://localhost:7091/usuarios/"
                        + notificacionDTO.getIdUsuario()
                        + "/exists",
                Boolean.class
        );

        if (usuarioExiste == null || !usuarioExiste) {
            throw new ResourceNotFoundException("El usuario no existe");
        }

        Boolean reservaExiste = restTemplate.getForObject(
                "http://localhost:7093/reservas/"
                        + notificacionDTO.getIdReserva()
                        + "/exists",
                Boolean.class
        );

        if (reservaExiste == null || !reservaExiste) {
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

        return notificacionRepository.save(notificacion);
    }

    public List<Notificacion> listar() {
        return notificacionRepository.findAll();
    }

    public Notificacion buscarPorId(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notificación no encontrada"));
    }

    public Notificacion actualizar(
            Long id,
            NotificacionDTO notificacionDTO) {

        Notificacion notificacion = buscarPorId(id);

        notificacion.setIdUsuario(notificacionDTO.getIdUsuario());
        notificacion.setIdReserva(notificacionDTO.getIdReserva());
        notificacion.setMensaje(notificacionDTO.getMensaje());
        notificacion.setTipoNotificacion(
                notificacionDTO.getTipoNotificacion());
        notificacion.setFechaEnvio(notificacionDTO.getFechaEnvio());
        notificacion.setEstado(notificacionDTO.getEstado());

        return notificacionRepository.save(notificacion);
    }

    public void eliminar(Long id) {

        Notificacion notificacion = buscarPorId(id);

        notificacionRepository.delete(notificacion);
    }

    public boolean existePorId(Long id) {
        return notificacionRepository.existsById(id);
    }
}