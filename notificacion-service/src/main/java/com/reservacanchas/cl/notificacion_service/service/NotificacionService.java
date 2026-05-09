package com.reservacanchas.cl.notificacion_service.service;

import com.reservacanchas.cl.notificacion_service.model.Notificacion;
import com.reservacanchas.cl.notificacion_service.repository.NotificacionRepository;
import com.reservacanchas.cl.notificacion_service.exception.RecursoNoEncontradoException;
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
                "http://localhost:9091/usuarios/" + notificacion.getIdUsuario() + "/exists",
                Boolean.class
        );

        if (usuarioExiste == null || !usuarioExiste) {
            throw new RecursoNoEncontradoException("El usuario no existe");
        }

        Boolean reservaExiste = restTemplate.getForObject(
                "http://localhost:9093/reservas/" + notificacion.getIdReserva() + "/exists",
                Boolean.class
        );

        if (reservaExiste == null || !reservaExiste) {
            throw new RecursoNoEncontradoException("La reserva no existe");
        }

        notificacion.setEstado("ENVIADA");
        return notificacionRepository.save(notificacion);
    }

    public List<Notificacion> listar() {
        return notificacionRepository.findAll();
    }

    public boolean existePorId(Long id) {
        return notificacionRepository.existsById(id);
    }
}