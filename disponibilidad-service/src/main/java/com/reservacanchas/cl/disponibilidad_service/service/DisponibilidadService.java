package com.reservacanchas.cl.disponibilidad_service.service;

import com.reservacanchas.cl.disponibilidad_service.exception.BadRequestException;
import com.reservacanchas.cl.disponibilidad_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.disponibilidad_service.model.Disponibilidad;
import com.reservacanchas.cl.disponibilidad_service.repository.DisponibilidadRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DisponibilidadService {

    private static final Logger logger = LoggerFactory.getLogger(DisponibilidadService.class);

    private static final String CANCHA_SERVICE_URL = "http://localhost:7092/canchas/";

    private final DisponibilidadRepository disponibilidadRepository;
    private final RestTemplate restTemplate;

    public DisponibilidadService(DisponibilidadRepository disponibilidadRepository) {
        this.disponibilidadRepository = disponibilidadRepository;
        this.restTemplate = new RestTemplate();
    }

    public Disponibilidad guardar(Disponibilidad disponibilidad) {

        logger.info("Iniciando creación de disponibilidad para cancha ID: {}",
                disponibilidad.getIdCancha());

        validarFecha(disponibilidad);
        validarCanchaExiste(disponibilidad.getIdCancha());

        Disponibilidad disponibilidadGuardada = disponibilidadRepository.save(disponibilidad);

        logger.info("Disponibilidad creada correctamente con ID: {}",
                disponibilidadGuardada.getId());

        return disponibilidadGuardada;
    }

    public List<Disponibilidad> listar() {

        logger.info("Iniciando búsqueda de todas las disponibilidades");

        List<Disponibilidad> disponibilidades = disponibilidadRepository.findAll();

        logger.info("Búsqueda finalizada. Total de disponibilidades encontradas: {}",
                disponibilidades.size());

        return disponibilidades;
    }

    public Disponibilidad buscarPorId(Long id) {

        logger.info("Iniciando búsqueda de disponibilidad con ID: {}", id);

        return disponibilidadRepository.findById(id)
                .map(disponibilidad -> {
                    logger.info("Disponibilidad encontrada correctamente con ID: {}", id);
                    return disponibilidad;
                })
                .orElseThrow(() -> {
                    logger.warn("Disponibilidad no encontrada con ID: {}", id);
                    return new ResourceNotFoundException("Disponibilidad no encontrada");
                });
    }

    public Disponibilidad actualizar(Long id, Disponibilidad disponibilidadActualizada) {

        logger.info("Iniciando actualización de disponibilidad con ID: {}", id);

        Disponibilidad disponibilidad = buscarPorId(id);

        validarFecha(disponibilidadActualizada);
        validarCanchaExiste(disponibilidadActualizada.getIdCancha());

        disponibilidad.setIdCancha(disponibilidadActualizada.getIdCancha());
        disponibilidad.setFecha(disponibilidadActualizada.getFecha());
        disponibilidad.setHoraInicio(disponibilidadActualizada.getHoraInicio());
        disponibilidad.setHoraFin(disponibilidadActualizada.getHoraFin());
        disponibilidad.setEstado(disponibilidadActualizada.getEstado());

        Disponibilidad disponibilidadGuardada = disponibilidadRepository.save(disponibilidad);

        logger.info("Disponibilidad actualizada correctamente con ID: {}", id);

        return disponibilidadGuardada;
    }

    public void eliminar(Long id) {

        logger.warn("Iniciando eliminación de disponibilidad con ID: {}", id);

        Disponibilidad disponibilidad = buscarPorId(id);

        disponibilidadRepository.delete(disponibilidad);

        logger.info("Disponibilidad eliminada correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de disponibilidad con ID: {}", id);

        boolean existe = disponibilidadRepository.existsById(id);

        logger.info("Resultado de existencia para disponibilidad con ID {}: {}", id, existe);

        return existe;
    }

    private void validarFecha(Disponibilidad disponibilidad) {

        logger.debug("Validando fecha de la disponibilidad");

        if (disponibilidad.getFecha() == null) {
            logger.warn("Validación fallida: la fecha de la disponibilidad es nula");
            throw new BadRequestException("La fecha es obligatoria");
        }

        logger.debug("Fecha de disponibilidad validada correctamente");
    }

    private void validarCanchaExiste(Long idCancha) {

        logger.info("Validando existencia de cancha con ID: {}", idCancha);

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