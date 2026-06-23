package com.reservacanchas.cl.reserva_service.service;

import com.reservacanchas.cl.reserva_service.dto.ReservaDTO;
import com.reservacanchas.cl.reserva_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.reserva_service.model.Reserva;
import com.reservacanchas.cl.reserva_service.repository.ReservaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ReservaService {

    private static final Logger logger =
            LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository reservaRepository;
    private final RestTemplate restTemplate;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
        this.restTemplate = new RestTemplate();
    }

    public Reserva guardar(ReservaDTO reservaDTO) {

        logger.info("Intentando crear reserva para usuario {} y cancha {}",
                reservaDTO.getIdUsuario(),
                reservaDTO.getIdCancha());

        Boolean usuarioExiste = restTemplate.getForObject(
                "http://localhost:7091/usuarios/" + reservaDTO.getIdUsuario() + "/exists",
                Boolean.class
        );

        if (usuarioExiste == null || !usuarioExiste) {

            logger.error("No se pudo crear reserva. Usuario {} no existe",
                    reservaDTO.getIdUsuario());

            throw new ResourceNotFoundException("El usuario no existe");
        }

        Boolean canchaExiste = restTemplate.getForObject(
                "http://localhost:7092/canchas/" + reservaDTO.getIdCancha() + "/exists",
                Boolean.class
        );

        if (canchaExiste == null || !canchaExiste) {

            logger.error("No se pudo crear reserva. Cancha {} no existe",
                    reservaDTO.getIdCancha());

            throw new ResourceNotFoundException("La cancha no existe");
        }

        Reserva reserva = new Reserva();

        reserva.setIdUsuario(reservaDTO.getIdUsuario());
        reserva.setIdCancha(reservaDTO.getIdCancha());
        reserva.setTotal(reservaDTO.getTotal());
        reserva.setEstado("CONFIRMADA");

        Reserva reservaGuardada = reservaRepository.save(reserva);

        logger.info("Reserva creada correctamente con ID: {}",
                reservaGuardada.getId());

        return reservaGuardada;
    }

    public List<Reserva> listar() {

        logger.info("Listando todas las reservas");

        return reservaRepository.findAll();
    }

    public Reserva buscarPorId(Long id) {

        logger.info("Buscando reserva con ID: {}", id);

        return reservaRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Reserva no encontrada con ID: {}", id);

                    return new ResourceNotFoundException(
                            "Reserva no encontrada");
                });
    }

    public Reserva actualizar(Long id, ReservaDTO reservaDTO) {

        logger.info("Actualizando reserva con ID: {}", id);

        Reserva reserva = buscarPorId(id);

        reserva.setIdUsuario(reservaDTO.getIdUsuario());
        reserva.setIdCancha(reservaDTO.getIdCancha());
        reserva.setTotal(reservaDTO.getTotal());

        Reserva reservaActualizada = reservaRepository.save(reserva);

        logger.info("Reserva actualizada correctamente con ID: {}", id);

        return reservaActualizada;
    }

    public void eliminar(Long id) {

        logger.info("Eliminando reserva con ID: {}", id);

        Reserva reserva = buscarPorId(id);

        reservaRepository.delete(reserva);

        logger.info("Reserva eliminada correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de reserva con ID: {}", id);

        boolean existe = reservaRepository.existsById(id);

        logger.info("Resultado existencia reserva {}: {}", id, existe);

        return existe;
    }

    public List<Reserva> buscarPorEstado(String estado) {

        logger.info("Buscando reservas por estado: {}", estado);

        return reservaRepository.findByEstado(estado);
    }

    public List<Reserva> buscarPorUsuario(Long idUsuario) {

        logger.info("Buscando reservas del usuario ID: {}", idUsuario);

        return reservaRepository.findByIdUsuario(idUsuario);
    }

    public List<Reserva> buscarPorCancha(Long idCancha) {

        logger.info("Buscando reservas de la cancha ID: {}", idCancha);

        return reservaRepository.findByIdCancha(idCancha);
    }
}