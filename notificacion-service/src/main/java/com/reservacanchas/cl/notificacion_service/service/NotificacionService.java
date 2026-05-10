package com.reservacanchas.cl.notificacion_service.service;

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

    public Notificacion guardar(Notificacion notificacion) {

        Boolean usuarioExiste = restTemplate.getForObject(
                "http://localhost:7091/usuarios/" + notificacion.getIdUsuario() + "/exists",
                Boolean.class
        );

        if (usuarioExiste == null || !usuarioExiste) {
            throw new ResourceNotFoundException("El usuario no existe");
        }

        Boolean reservaExiste = restTemplate.getForObject(
                "http://localhost:7093/reservas/" + notificacion.getIdReserva() + "/exists",
                Boolean.class
        );

        if (reservaExiste == null || !reservaExiste) {
            throw new ResourceNotFoundException("La reserva no existe");
        }

        notificacion.setEstado("ENVIADA");
        return notificacionRepository.save(notificacion);
    }

    public List<Notificacion> listar() {
        return notificacionRepository.findAll();
    }

    public Notificacion buscarPorId(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notificación no encontrada"));
    }

    public Notificacion actualizar(Long id, Notificacion notificacionActualizada) {
        Notificacion notificacion = buscarPorId(id);

        notificacion.setIdUsuario(notificacionActualizada.getIdUsuario());
        notificacion.setIdReserva(notificacionActualizada.getIdReserva());
        notificacion.setMensaje(notificacionActualizada.getMensaje());
        notificacion.setTipoNotificacion(notificacionActualizada.getTipoNotificacion());
        notificacion.setFechaEnvio(notificacionActualizada.getFechaEnvio());
        notificacion.setEstado(notificacionActualizada.getEstado());

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