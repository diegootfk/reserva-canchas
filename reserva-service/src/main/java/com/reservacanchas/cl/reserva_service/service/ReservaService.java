package com.reservacanchas.cl.reserva_service.service;

import com.reservacanchas.cl.reserva_service.dto.ReservaDTO;
import com.reservacanchas.cl.reserva_service.model.Reserva;
import com.reservacanchas.cl.reserva_service.repository.ReservaRepository;
import com.reservacanchas.cl.reserva_service.exception.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final RestTemplate restTemplate;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
        this.restTemplate = new RestTemplate();
    }

    public Reserva guardar(ReservaDTO reservaDTO) {

        Boolean usuarioExiste = restTemplate.getForObject(
                "http://localhost:9091/usuarios/" + reservaDTO.getIdUsuario() + "/exists",
                Boolean.class
        );

        if (usuarioExiste == null || !usuarioExiste) {
            throw new RecursoNoEncontradoException("El usuario no existe");
        }

        Boolean canchaExiste = restTemplate.getForObject(
                "http://localhost:9092/canchas/" + reservaDTO.getIdCancha() + "/exists",
                Boolean.class
        );

        if (canchaExiste == null || !canchaExiste) {
            throw new RecursoNoEncontradoException("La cancha no existe");
        }

        Reserva reserva = new Reserva();
        reserva.setIdUsuario(reservaDTO.getIdUsuario());
        reserva.setIdCancha(reservaDTO.getIdCancha());
        reserva.setTotal(reservaDTO.getTotal());
        reserva.setEstado("CONFIRMADA");

        return reservaRepository.save(reserva);
    }

    public List<Reserva> listar() {
        return reservaRepository.findAll();
    }

    public boolean existePorId(Long id) {
        return reservaRepository.existsById(id);
    }
}