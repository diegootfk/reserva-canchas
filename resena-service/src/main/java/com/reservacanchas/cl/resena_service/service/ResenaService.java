package com.reservacanchas.cl.resena_service.service;

import com.reservacanchas.cl.resena_service.model.Resena;
import com.reservacanchas.cl.resena_service.repository.ResenaRepository;
import com.reservacanchas.cl.resena_service.exception.RecursoNoEncontradoException;
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

    public Resena guardar(Resena resena) {

        Boolean usuarioExiste = restTemplate.getForObject(
                "http://localhost:9091/usuarios/" + resena.getIdUsuario() + "/exists",
                Boolean.class
        );

        Boolean canchaExiste = restTemplate.getForObject(
                "http://localhost:9092/canchas/" + resena.getIdCancha() + "/exists",
                Boolean.class
        );

        Boolean reservaExiste = restTemplate.getForObject(
                "http://localhost:9093/reservas/" + resena.getIdReserva() + "/exists",
                Boolean.class
        );

        if (usuarioExiste == null || !usuarioExiste)
            throw new RecursoNoEncontradoException("Usuario no existe");

        if (canchaExiste == null || !canchaExiste)
            throw new RecursoNoEncontradoException("Cancha no existe");

        if (reservaExiste == null || !reservaExiste)
            throw new RecursoNoEncontradoException("Reserva no existe");

        return resenaRepository.save(resena);
    }

    public List<Resena> listar() {
        return resenaRepository.findAll();
    }

    public boolean existePorId(Long id) {
        return resenaRepository.existsById(id);
    }
}