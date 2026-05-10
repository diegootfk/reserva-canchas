package com.reservacanchas.cl.reserva_service.service;

import com.reservacanchas.cl.reserva_service.dto.ReservaDTO;
import com.reservacanchas.cl.reserva_service.exception.RecursoNoEncontradoException;
import com.reservacanchas.cl.reserva_service.model.Reserva;
import com.reservacanchas.cl.reserva_service.repository.ReservaRepository;
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

    public Reserva buscarPorId(Long id) {
        return reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva no encontrada"));
    }

    public Reserva actualizar(Long id, ReservaDTO reservaDTO) {
        Reserva reserva = buscarPorId(id);

        reserva.setIdUsuario(reservaDTO.getIdUsuario());
        reserva.setIdCancha(reservaDTO.getIdCancha());
        reserva.setTotal(reservaDTO.getTotal());

        return reservaRepository.save(reserva);
    }

    public void eliminar(Long id) {
        Reserva reserva = buscarPorId(id);
        reservaRepository.delete(reserva);
    }

    public boolean existePorId(Long id) {
        return reservaRepository.existsById(id);
    }

    public List<Reserva> buscarPorEstado(String estado) {
        return reservaRepository.findByEstado(estado);
    }

    public List<Reserva> buscarPorUsuario(Long idUsuario) {
        return reservaRepository.findByIdUsuario(idUsuario);
    }

    public List<Reserva> buscarPorCancha(Long idCancha) {
        return reservaRepository.findByIdCancha(idCancha);
    }
}