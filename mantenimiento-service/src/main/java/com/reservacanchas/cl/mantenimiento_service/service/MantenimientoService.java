package com.reservacanchas.cl.mantenimiento_service.service;

import com.reservacanchas.cl.mantenimiento_service.dto.MantenimientoDTO;
import com.reservacanchas.cl.mantenimiento_service.exception.BadRequestException;
import com.reservacanchas.cl.mantenimiento_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.mantenimiento_service.model.Mantenimiento;
import com.reservacanchas.cl.mantenimiento_service.repository.MantenimientoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class MantenimientoService {

    private static final Logger logger =
            LoggerFactory.getLogger(MantenimientoService.class);

    private final MantenimientoRepository mantenimientoRepository;
    private final WebClient.Builder webClientBuilder;

    public MantenimientoService(
            MantenimientoRepository mantenimientoRepository,
            WebClient.Builder webClientBuilder) {

        this.mantenimientoRepository = mantenimientoRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public Mantenimiento guardar(MantenimientoDTO mantenimientoDTO) {

        logger.info(
                "Iniciando creación de mantenimiento para cancha {}",
                mantenimientoDTO.getIdCancha()
        );

        if (mantenimientoDTO.getDescripcion() == null
                || mantenimientoDTO.getDescripcion().isBlank()) {

            logger.warn(
                    "No se pudo crear mantenimiento: descripción vacía"
            );

            throw new BadRequestException(
                    "La descripción del mantenimiento es obligatoria");
        }

        Boolean canchaExiste = webClientBuilder.build()
                .get()
                .uri("http://cancha-service:7092/canchas/"
                        + mantenimientoDTO.getIdCancha()
                        + "/exists")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (canchaExiste == null || !canchaExiste) {

            logger.warn(
                    "No se pudo crear mantenimiento. Cancha {} no existe",
                    mantenimientoDTO.getIdCancha()
            );

            throw new ResourceNotFoundException(
                    "La cancha no existe"
            );
        }

        Mantenimiento mantenimiento = new Mantenimiento();

        mantenimiento.setIdCancha(mantenimientoDTO.getIdCancha());
        mantenimiento.setFechaInicio(mantenimientoDTO.getFechaInicio());
        mantenimiento.setFechaFin(mantenimientoDTO.getFechaFin());
        mantenimiento.setDescripcion(mantenimientoDTO.getDescripcion());
        mantenimiento.setEstado(mantenimientoDTO.getEstado());

        Mantenimiento mantenimientoGuardado =
                mantenimientoRepository.save(mantenimiento);

        logger.info(
                "Mantenimiento creado correctamente con ID {}",
                mantenimientoGuardado.getId()
        );

        return mantenimientoGuardado;
    }

    public List<Mantenimiento> listar() {

        logger.info("Listando todos los mantenimientos");

        List<Mantenimiento> mantenimientos =
                mantenimientoRepository.findAll();

        logger.info(
                "Se encontraron {} mantenimientos",
                mantenimientos.size()
        );

        return mantenimientos;
    }

    public Mantenimiento buscarPorId(Long id) {

        logger.info(
                "Buscando mantenimiento con ID {}",
                id
        );

        return mantenimientoRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error(
                            "Mantenimiento con ID {} no encontrado",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Mantenimiento no encontrado"
                    );
                });
    }

    public Mantenimiento actualizar(
            Long id,
            MantenimientoDTO mantenimientoDTO) {

        logger.info(
                "Actualizando mantenimiento con ID {}",
                id
        );

        Mantenimiento mantenimiento = buscarPorId(id);

        mantenimiento.setIdCancha(mantenimientoDTO.getIdCancha());
        mantenimiento.setFechaInicio(mantenimientoDTO.getFechaInicio());
        mantenimiento.setFechaFin(mantenimientoDTO.getFechaFin());
        mantenimiento.setDescripcion(mantenimientoDTO.getDescripcion());
        mantenimiento.setEstado(mantenimientoDTO.getEstado());

        Mantenimiento mantenimientoActualizado =
                mantenimientoRepository.save(mantenimiento);

        logger.info(
                "Mantenimiento actualizado correctamente con ID {}",
                id
        );

        return mantenimientoActualizado;
    }

    public void eliminar(Long id) {

        logger.info(
                "Eliminando mantenimiento con ID {}",
                id
        );

        Mantenimiento mantenimiento = buscarPorId(id);

        mantenimientoRepository.delete(mantenimiento);

        logger.info(
                "Mantenimiento eliminado correctamente con ID {}",
                id
        );
    }

    public boolean existePorId(Long id) {

        logger.info(
                "Verificando existencia de mantenimiento con ID {}",
                id
        );

        boolean existe = mantenimientoRepository.existsById(id);

        logger.info(
                "Resultado existencia mantenimiento {}: {}",
                id,
                existe
        );

        return existe;
    }
}