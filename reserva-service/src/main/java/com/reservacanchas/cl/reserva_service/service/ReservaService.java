package com.reservacanchas.cl.reserva_service.service;

import com.reservacanchas.cl.reserva_service.dto.ReservaDTO;
import com.reservacanchas.cl.reserva_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.reserva_service.model.Reserva;
import com.reservacanchas.cl.reserva_service.repository.ReservaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    private static final String USUARIO_SERVICE_URL = "http://localhost:7091/usuarios/";
    private static final String CANCHA_SERVICE_URL = "http://localhost:7092/canchas/";

    private final ReservaRepository reservaRepository;
    private final RestTemplate restTemplate;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
        this.restTemplate = new RestTemplate();
    }

    public Reserva guardar(ReservaDTO reservaDTO) {

        logger.info(
                "Iniciando creación de reserva para usuario ID: {} y cancha ID: {}",
                reservaDTO.getIdUsuario(),
                reservaDTO.getIdCancha()
        );

        validarUsuarioExiste(reservaDTO.getIdUsuario());
        validarCanchaExiste(reservaDTO.getIdCancha());

        Reserva reserva = new Reserva();

        reserva.setIdUsuario(reservaDTO.getIdUsuario());
        reserva.setIdCancha(reservaDTO.getIdCancha());
        reserva.setTotal(reservaDTO.getTotal());
        reserva.setEstado("CONFIRMADA");

        Reserva reservaGuardada = reservaRepository.save(reserva);

        logger.info("Reserva creada correctamente con ID: {}", reservaGuardada.getId());

        return reservaGuardada;
    }

    public List<Reserva> listar() {

        logger.info("Iniciando búsqueda de todas las reservas");

        List<Reserva> reservas = reservaRepository.findAll();

        logger.info("Búsqueda finalizada. Total de reservas encontradas: {}", reservas.size());

        return reservas;
    }

    public Reserva buscarPorId(Long id) {

        logger.info("Iniciando búsqueda de reserva con ID: {}", id);

        return reservaRepository.findById(id)
                .map(reserva -> {
                    logger.info("Reserva encontrada correctamente con ID: {}", id);
                    return reserva;
                })
                .orElseThrow(() -> {
                    logger.warn("Reserva no encontrada con ID: {}", id);
                    return new ResourceNotFoundException("Reserva no encontrada");
                });
    }

    public Reserva actualizar(Long id, ReservaDTO reservaDTO) {

        logger.info("Iniciando actualización de reserva con ID: {}", id);

        Reserva reserva = buscarPorId(id);

        validarUsuarioExiste(reservaDTO.getIdUsuario());
        validarCanchaExiste(reservaDTO.getIdCancha());

        reserva.setIdUsuario(reservaDTO.getIdUsuario());
        reserva.setIdCancha(reservaDTO.getIdCancha());
        reserva.setTotal(reservaDTO.getTotal());

        Reserva reservaActualizada = reservaRepository.save(reserva);

        logger.info("Reserva actualizada correctamente con ID: {}", id);

        return reservaActualizada;
    }

    public void eliminar(Long id) {

        logger.warn("Iniciando eliminación de reserva con ID: {}", id);

        Reserva reserva = buscarPorId(id);

        reservaRepository.delete(reserva);

        logger.info("Reserva eliminada correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de reserva con ID: {}", id);

        boolean existe = reservaRepository.existsById(id);

        logger.info("Resultado de existencia para reserva con ID {}: {}", id, existe);

        return existe;
    }

    public List<Reserva> buscarPorEstado(String estado) {

        logger.info("Buscando reservas con estado: {}", estado);

        List<Reserva> reservas = reservaRepository.findByEstado(estado);

        logger.info("Se encontraron {} reservas con estado: {}", reservas.size(), estado);

        return reservas;
    }

    public List<Reserva> buscarPorUsuario(Long idUsuario) {

        logger.info("Buscando reservas asociadas al usuario con ID: {}", idUsuario);

        List<Reserva> reservas = reservaRepository.findByIdUsuario(idUsuario);

        logger.info("Se encontraron {} reservas para el usuario con ID: {}", reservas.size(), idUsuario);

        return reservas;
    }

    public List<Reserva> buscarPorCancha(Long idCancha) {

        logger.info("Buscando reservas asociadas a la cancha con ID: {}", idCancha);

        List<Reserva> reservas = reservaRepository.findByIdCancha(idCancha);

        logger.info("Se encontraron {} reservas para la cancha con ID: {}", reservas.size(), idCancha);

        return reservas;
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
                throw new ResourceNotFoundException("El usuario no existe");
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
                throw new ResourceNotFoundException("La cancha no existe");
            }

            logger.info("Cancha validada correctamente con ID: {}", idCancha);

        } catch (RestClientException ex) {
            logger.error("Error remoto al validar cancha con ID: {}", idCancha, ex);
            throw new ResourceNotFoundException("No se pudo validar la existencia de la cancha");
        }
    }
}