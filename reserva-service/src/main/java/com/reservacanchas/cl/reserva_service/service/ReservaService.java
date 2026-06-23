package com.reservacanchas.cl.reserva_service.service;

import com.reservacanchas.cl.reserva_service.dto.ReservaDTO;
import com.reservacanchas.cl.reserva_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.reserva_service.model.Reserva;
import com.reservacanchas.cl.reserva_service.repository.ReservaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ReservaService {

    private static final Logger logger =
            LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository reservaRepository;
    private final WebClient.Builder webClientBuilder;

    public ReservaService(
            ReservaRepository reservaRepository,
            WebClient.Builder webClientBuilder) {

        this.reservaRepository = reservaRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public Reserva guardar(ReservaDTO reservaDTO) {

        logger.info(
                "Iniciando creación de reserva para usuario {} y cancha {}",
                reservaDTO.getIdUsuario(),
                reservaDTO.getIdCancha()
        );

        Boolean usuarioExiste = webClientBuilder.build()
                .get()
                .uri("http://localhost:7091/usuarios/"
                        + reservaDTO.getIdUsuario()
                        + "/exists")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (usuarioExiste == null || !usuarioExiste) {

            logger.warn(
                    "No se pudo crear reserva. Usuario {} no existe",
                    reservaDTO.getIdUsuario()
            );

            throw new ResourceNotFoundException(
                    "El usuario no existe"
            );
        }

        Boolean canchaExiste = webClientBuilder.build()
                .get()
                .uri("http://localhost:7092/canchas/"
                        + reservaDTO.getIdCancha()
                        + "/exists")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (canchaExiste == null || !canchaExiste) {

            logger.warn(
                    "No se pudo crear reserva. Cancha {} no existe",
                    reservaDTO.getIdCancha()
            );

            throw new ResourceNotFoundException(
                    "La cancha no existe"
            );
        }

        Reserva reserva = new Reserva();

        reserva.setIdUsuario(reservaDTO.getIdUsuario());
        reserva.setIdCancha(reservaDTO.getIdCancha());
        reserva.setTotal(reservaDTO.getTotal());
        reserva.setEstado("CONFIRMADA");

        Reserva reservaGuardada = reservaRepository.save(reserva);

        logger.info(
                "Reserva creada correctamente con ID {}",
                reservaGuardada.getId()
        );

        return reservaGuardada;
    }

    public List<Reserva> listar() {

        logger.info("Listando todas las reservas");

        List<Reserva> reservas = reservaRepository.findAll();

        logger.info(
                "Se encontraron {} reservas",
                reservas.size()
        );

        return reservas;
    }

    public Reserva buscarPorId(Long id) {

        logger.info("Buscando reserva con ID {}", id);

        return reservaRepository.findById(id)
                .orElseThrow(() -> {

                    logger.warn(
                            "Reserva con ID {} no encontrada",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Reserva no encontrada"
                    );
                });
    }

    public Reserva actualizar(Long id, ReservaDTO reservaDTO) {

        logger.info("Actualizando reserva con ID {}", id);

        Reserva reserva = buscarPorId(id);

        reserva.setIdUsuario(reservaDTO.getIdUsuario());
        reserva.setIdCancha(reservaDTO.getIdCancha());
        reserva.setTotal(reservaDTO.getTotal());

        Reserva reservaActualizada =
                reservaRepository.save(reserva);

        logger.info(
                "Reserva con ID {} actualizada correctamente",
                id
        );

        return reservaActualizada;
    }

    public void eliminar(Long id) {

        logger.info("Eliminando reserva con ID {}", id);

        Reserva reserva = buscarPorId(id);

        reservaRepository.delete(reserva);

        logger.info(
                "Reserva con ID {} eliminada correctamente",
                id
        );
    }

    public boolean existePorId(Long id) {

        logger.info(
                "Verificando existencia de reserva con ID {}",
                id
        );

        boolean existe = reservaRepository.existsById(id);

        logger.info(
                "Resultado existencia reserva {}: {}",
                id,
                existe
        );

        return existe;
    }

    public List<Reserva> buscarPorEstado(String estado) {

        logger.info(
                "Buscando reservas por estado {}",
                estado
        );

        return reservaRepository.findByEstado(estado);
    }

    public List<Reserva> buscarPorUsuario(Long idUsuario) {

        logger.info(
                "Buscando reservas del usuario {}",
                idUsuario
        );

        return reservaRepository.findByIdUsuario(idUsuario);
    }

    public List<Reserva> buscarPorCancha(Long idCancha) {

        logger.info(
                "Buscando reservas de la cancha {}",
                idCancha
        );

        return reservaRepository.findByIdCancha(idCancha);
    }
}