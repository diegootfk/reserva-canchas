package com.reservacanchas.cl.disponibilidad_service.service;

import com.reservacanchas.cl.disponibilidad_service.dto.DisponibilidadDTO;
import com.reservacanchas.cl.disponibilidad_service.exception.BadRequestException;
import com.reservacanchas.cl.disponibilidad_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.disponibilidad_service.model.Disponibilidad;
import com.reservacanchas.cl.disponibilidad_service.repository.DisponibilidadRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DisponibilidadService {

    private static final Logger logger =
            LoggerFactory.getLogger(DisponibilidadService.class);

    private final DisponibilidadRepository disponibilidadRepository;
    private final RestTemplate restTemplate;

    public DisponibilidadService(DisponibilidadRepository disponibilidadRepository) {
        this.disponibilidadRepository = disponibilidadRepository;
        this.restTemplate = new RestTemplate();
    }

    public Disponibilidad guardar(DisponibilidadDTO disponibilidadDTO) {

        logger.info("Intentando crear disponibilidad para cancha {}",
                disponibilidadDTO.getIdCancha());

        if (disponibilidadDTO.getFecha() == null
                || disponibilidadDTO.getFecha().isBlank()) {

            logger.error("Error al crear disponibilidad: fecha vacía");

            throw new BadRequestException("La fecha es obligatoria");
        }

        Boolean canchaExiste = restTemplate.getForObject(
                "http://localhost:7092/canchas/"
                        + disponibilidadDTO.getIdCancha()
                        + "/exists",
                Boolean.class
        );

        if (canchaExiste == null || !canchaExiste) {

            logger.warn("No se pudo crear disponibilidad. Cancha {} no existe",
                    disponibilidadDTO.getIdCancha());

            throw new ResourceNotFoundException("La cancha no existe");
        }

        Disponibilidad disponibilidad = new Disponibilidad();

        disponibilidad.setIdCancha(disponibilidadDTO.getIdCancha());
        disponibilidad.setFecha(disponibilidadDTO.getFecha());
        disponibilidad.setHoraInicio(disponibilidadDTO.getHoraInicio());
        disponibilidad.setHoraFin(disponibilidadDTO.getHoraFin());
        disponibilidad.setEstado(disponibilidadDTO.getEstado());

        Disponibilidad disponibilidadGuardada =
                disponibilidadRepository.save(disponibilidad);

        logger.info("Disponibilidad creada correctamente con ID {}",
                disponibilidadGuardada.getId());

        return disponibilidadGuardada;
    }

    public List<Disponibilidad> listar() {

        logger.info("Listando todas las disponibilidades");

        return disponibilidadRepository.findAll();
    }

    public Disponibilidad buscarPorId(Long id) {

        logger.info("Buscando disponibilidad con ID {}", id);

        return disponibilidadRepository.findById(id)
                .orElseThrow(() -> {

                    logger.warn("Disponibilidad con ID {} no encontrada", id);

                    return new ResourceNotFoundException(
                            "Disponibilidad no encontrada");
                });
    }

    public Disponibilidad actualizar(
            Long id,
            DisponibilidadDTO disponibilidadDTO) {

        logger.info("Actualizando disponibilidad con ID {}", id);

        Disponibilidad disponibilidad = buscarPorId(id);

        disponibilidad.setIdCancha(disponibilidadDTO.getIdCancha());
        disponibilidad.setFecha(disponibilidadDTO.getFecha());
        disponibilidad.setHoraInicio(disponibilidadDTO.getHoraInicio());
        disponibilidad.setHoraFin(disponibilidadDTO.getHoraFin());
        disponibilidad.setEstado(disponibilidadDTO.getEstado());

        Disponibilidad disponibilidadActualizada =
                disponibilidadRepository.save(disponibilidad);

        logger.info("Disponibilidad con ID {} actualizada correctamente", id);

        return disponibilidadActualizada;
    }

    public void eliminar(Long id) {

        logger.info("Eliminando disponibilidad con ID {}", id);

        Disponibilidad disponibilidad = buscarPorId(id);

        disponibilidadRepository.delete(disponibilidad);

        logger.info("Disponibilidad con ID {} eliminada correctamente", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de disponibilidad con ID {}", id);

        return disponibilidadRepository.existsById(id);
    }
}