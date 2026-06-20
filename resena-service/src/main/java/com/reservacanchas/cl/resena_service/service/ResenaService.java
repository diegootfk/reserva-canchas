package com.reservacanchas.cl.resena_service.service;

import com.reservacanchas.cl.resena_service.dto.ResenaDTO;
import com.reservacanchas.cl.resena_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.resena_service.model.Resena;
import com.reservacanchas.cl.resena_service.repository.ResenaRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final RestTemplate restTemplate;

    public ResenaService(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
        this.restTemplate = new RestTemplate();
    }

    public Resena guardar(ResenaDTO resenaDTO) {

        Boolean usuarioExiste = restTemplate.getForObject(
                "http://localhost:7091/usuarios/" + resenaDTO.getIdUsuario() + "/exists",
                Boolean.class
        );

        if (usuarioExiste == null || !usuarioExiste) {
            throw new ResourceNotFoundException("Usuario no existe");
        }

        Boolean canchaExiste = restTemplate.getForObject(
                "http://localhost:7092/canchas/" + resenaDTO.getIdCancha() + "/exists",
                Boolean.class
        );

        if (canchaExiste == null || !canchaExiste) {
            throw new ResourceNotFoundException("Cancha no existe");
        }

        Boolean reservaExiste = restTemplate.getForObject(
                "http://localhost:7093/reservas/" + resenaDTO.getIdReserva() + "/exists",
                Boolean.class
        );

        if (reservaExiste == null || !reservaExiste) {
            throw new ResourceNotFoundException("Reserva no existe");
        }

        Resena resena = new Resena();

        resena.setIdUsuario(resenaDTO.getIdUsuario());
        resena.setIdCancha(resenaDTO.getIdCancha());
        resena.setIdReserva(resenaDTO.getIdReserva());
        resena.setCalificacion(resenaDTO.getCalificacion());
        resena.setComentario(resenaDTO.getComentario());
        resena.setFechaResena(resenaDTO.getFechaResena());

        return resenaRepository.save(resena);
    }

    public List<Resena> listar() {
        return resenaRepository.findAll();
    }

    public Resena buscarPorId(Long id) {
        return resenaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reseña no encontrada"));
    }

    public Resena actualizar(Long id, ResenaDTO resenaDTO) {

        Resena resena = buscarPorId(id);

        resena.setIdUsuario(resenaDTO.getIdUsuario());
        resena.setIdCancha(resenaDTO.getIdCancha());
        resena.setIdReserva(resenaDTO.getIdReserva());
        resena.setCalificacion(resenaDTO.getCalificacion());
        resena.setComentario(resenaDTO.getComentario());
        resena.setFechaResena(resenaDTO.getFechaResena());

        return resenaRepository.save(resena);
    }

    public void eliminar(Long id) {

        Resena resena = buscarPorId(id);

        resenaRepository.delete(resena);
    }

    public boolean existePorId(Long id) {
        return resenaRepository.existsById(id);
    }
}