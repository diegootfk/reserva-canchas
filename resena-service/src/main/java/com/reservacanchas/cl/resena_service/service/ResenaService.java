package com.reservacanchas.cl.resena_service.service;

import com.reservacanchas.cl.resena_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.resena_service.model.Resena;
import com.reservacanchas.cl.resena_service.repository.ResenaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ResenaService {

    private static final Logger logger = LoggerFactory.getLogger(ResenaService.class);

    private static final String USUARIO_SERVICE_URL = "http://localhost:7091/usuarios/";
    private static final String CANCHA_SERVICE_URL = "http://localhost:7092/canchas/";
    private static final String RESERVA_SERVICE_URL = "http://localhost:7093/reservas/";

    private final ResenaRepository resenaRepository;
    private final RestTemplate restTemplate;

    public ResenaService(ResenaRepository resenaRepository) {
        this.resenaRepository = resenaRepository;
        this.restTemplate = new RestTemplate();
    }

    public Resena guardar(Resena resena) {

        logger.info(
                "Iniciando creación de reseña para usuario ID: {}, cancha ID: {} y reserva ID: {}",
                resena.getIdUsuario(),
                resena.getIdCancha(),
                resena.getIdReserva()
        );

        validarUsuarioExiste(resena.getIdUsuario());
        validarCanchaExiste(resena.getIdCancha());
        validarReservaExiste(resena.getIdReserva());

        Resena resenaGuardada = resenaRepository.save(resena);

        logger.info("Reseña creada correctamente con ID: {}", resenaGuardada.getId());

        return resenaGuardada;
    }

    public List<Resena> listar() {

        logger.info("Iniciando búsqueda de todas las reseñas");

        List<Resena> resenas = resenaRepository.findAll();

        logger.info("Búsqueda finalizada. Total de reseñas encontradas: {}", resenas.size());

        return resenas;
    }

    public Resena buscarPorId(Long id) {

        logger.info("Iniciando búsqueda de reseña con ID: {}", id);

        return resenaRepository.findById(id)
                .map(resena -> {
                    logger.info("Reseña encontrada correctamente con ID: {}", id);
                    return resena;
                })
                .orElseThrow(() -> {
                    logger.warn("Reseña no encontrada con ID: {}", id);
                    return new ResourceNotFoundException("Reseña no encontrada");
                });
    }

    public Resena actualizar(Long id, Resena resenaActualizada) {

        logger.info("Iniciando actualización de reseña con ID: {}", id);

        Resena resena = buscarPorId(id);

        validarUsuarioExiste(resenaActualizada.getIdUsuario());
        validarCanchaExiste(resenaActualizada.getIdCancha());
        validarReservaExiste(resenaActualizada.getIdReserva());

        resena.setIdUsuario(resenaActualizada.getIdUsuario());
        resena.setIdCancha(resenaActualizada.getIdCancha());
        resena.setIdReserva(resenaActualizada.getIdReserva());
        resena.setCalificacion(resenaActualizada.getCalificacion());
        resena.setComentario(resenaActualizada.getComentario());
        resena.setFechaResena(resenaActualizada.getFechaResena());

        Resena resenaGuardada = resenaRepository.save(resena);

        logger.info("Reseña actualizada correctamente con ID: {}", id);

        return resenaGuardada;
    }

    public void eliminar(Long id) {

        logger.warn("Iniciando eliminación de reseña con ID: {}", id);

        Resena resena = buscarPorId(id);

        resenaRepository.delete(resena);

        logger.info("Reseña eliminada correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de reseña con ID: {}", id);

        boolean existe = resenaRepository.existsById(id);

        logger.info("Resultado de existencia para reseña con ID {}: {}", id, existe);

        return existe;
    }

    private void validarUsuarioExiste(Long idUsuario) {

        logger.info("Validando existencia del usuario con ID: {}", idUsuario);

        try {
            Boolean usuarioExiste = restTemplate.getForObject(
                    USUARIO_SERVICE_URL + idUsuario + "/exists",
                    Boolean.class
            );

            if (usuarioExiste == null || !usuarioExiste) {
                logger.warn("Validación fallida: usuario con ID {} no existe", idUsuario);
                throw new ResourceNotFoundException("Usuario no existe");
            }

            logger.info("Usuario validado correctamente con ID: {}", idUsuario);

        } catch (RestClientException ex) {
            logger.error("Error remoto al validar usuario con ID: {}", idUsuario, ex);
            throw new ResourceNotFoundException("No se pudo validar la existencia del usuario");
        }
    }

    private void validarCanchaExiste(Long idCancha) {

        logger.info("Validando existencia de la cancha con ID: {}", idCancha);

        try {
            Boolean canchaExiste = restTemplate.getForObject(
                    CANCHA_SERVICE_URL + idCancha + "/exists",
                    Boolean.class
            );

            if (canchaExiste == null || !canchaExiste) {
                logger.warn("Validación fallida: cancha con ID {} no existe", idCancha);
                throw new ResourceNotFoundException("Cancha no existe");
            }

            logger.info("Cancha validada correctamente con ID: {}", idCancha);

        } catch (RestClientException ex) {
            logger.error("Error remoto al validar cancha con ID: {}", idCancha, ex);
            throw new ResourceNotFoundException("No se pudo validar la existencia de la cancha");
        }
    }

    private void validarReservaExiste(Long idReserva) {

        logger.info("Validando existencia de la reserva con ID: {}", idReserva);

        try {
            Boolean reservaExiste = restTemplate.getForObject(
                    RESERVA_SERVICE_URL + idReserva + "/exists",
                    Boolean.class
            );

            if (reservaExiste == null || !reservaExiste) {
                logger.warn("Validación fallida: reserva con ID {} no existe", idReserva);
                throw new ResourceNotFoundException("Reserva no existe");
            }

            logger.info("Reserva validada correctamente con ID: {}", idReserva);

        } catch (RestClientException ex) {
            logger.error("Error remoto al validar reserva con ID: {}", idReserva, ex);
            throw new ResourceNotFoundException("No se pudo validar la existencia de la reserva");
        }
    }
}