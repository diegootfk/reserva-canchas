package com.reservacanchas.cl.resena_service.service;

import com.reservacanchas.cl.resena_service.dto.ResenaDTO;
import com.reservacanchas.cl.resena_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.resena_service.model.Resena;
import com.reservacanchas.cl.resena_service.repository.ResenaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ResenaService {

    private static final Logger logger =
            LoggerFactory.getLogger(ResenaService.class);

    private final ResenaRepository resenaRepository;
    private final RestTemplate restTemplate;

    public ResenaService(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
        this.restTemplate = new RestTemplate();
    }

    public Resena guardar(ResenaDTO resenaDTO) {

        logger.info(
                "Intentando crear reseña para usuario {}, cancha {} y reserva {}",
                resenaDTO.getIdUsuario(),
                resenaDTO.getIdCancha(),
                resenaDTO.getIdReserva()
        );

        Boolean usuarioExiste = restTemplate.getForObject(
                "http://localhost:7091/usuarios/" + resenaDTO.getIdUsuario() + "/exists",
                Boolean.class
        );

        if (usuarioExiste == null || !usuarioExiste) {

            logger.warn("Usuario {} no existe",
                    resenaDTO.getIdUsuario());

            throw new ResourceNotFoundException("Usuario no existe");
        }

        Boolean canchaExiste = restTemplate.getForObject(
                "http://localhost:7092/canchas/" + resenaDTO.getIdCancha() + "/exists",
                Boolean.class
        );

        if (canchaExiste == null || !canchaExiste) {

            logger.warn("Cancha {} no existe",
                    resenaDTO.getIdCancha());

            throw new ResourceNotFoundException("Cancha no existe");
        }

        Boolean reservaExiste = restTemplate.getForObject(
                "http://localhost:7093/reservas/" + resenaDTO.getIdReserva() + "/exists",
                Boolean.class
        );

        if (reservaExiste == null || !reservaExiste) {

            logger.warn("Reserva {} no existe",
                    resenaDTO.getIdReserva());

            throw new ResourceNotFoundException("Reserva no existe");
        }

        Resena resena = new Resena();

        resena.setIdUsuario(resenaDTO.getIdUsuario());
        resena.setIdCancha(resenaDTO.getIdCancha());
        resena.setIdReserva(resenaDTO.getIdReserva());
        resena.setCalificacion(resenaDTO.getCalificacion());
        resena.setComentario(resenaDTO.getComentario());
        resena.setFechaResena(resenaDTO.getFechaResena());

        Resena resenaGuardada = resenaRepository.save(resena);

        logger.info("Reseña creada correctamente con ID: {}",
                resenaGuardada.getId());

        return resenaGuardada;
    }

    public List<Resena> listar() {

        logger.info("Listando todas las reseñas");

        return resenaRepository.findAll();
    }

    public Resena buscarPorId(Long id) {

        logger.info("Buscando reseña con ID: {}", id);

        return resenaRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Reseña no encontrada con ID: {}", id);

                    return new ResourceNotFoundException(
                            "Reseña no encontrada");
                });
    }

    public Resena actualizar(Long id, ResenaDTO resenaDTO) {

        logger.info("Actualizando reseña con ID: {}", id);

        Resena resena = buscarPorId(id);

        resena.setIdUsuario(resenaDTO.getIdUsuario());
        resena.setIdCancha(resenaDTO.getIdCancha());
        resena.setIdReserva(resenaDTO.getIdReserva());
        resena.setCalificacion(resenaDTO.getCalificacion());
        resena.setComentario(resenaDTO.getComentario());
        resena.setFechaResena(resenaDTO.getFechaResena());

        Resena resenaActualizada = resenaRepository.save(resena);

        logger.info("Reseña actualizada correctamente con ID: {}", id);

        return resenaActualizada;
    }

    public void eliminar(Long id) {

        logger.info("Eliminando reseña con ID: {}", id);

        Resena resena = buscarPorId(id);

        resenaRepository.delete(resena);

        logger.info("Reseña eliminada correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de reseña con ID: {}", id);

        return resenaRepository.existsById(id);
    }
}