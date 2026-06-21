package com.reservacanchas.cl.mantenimiento_service.service;

import com.reservacanchas.cl.mantenimiento_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.mantenimiento_service.model.Mantenimiento;
import com.reservacanchas.cl.mantenimiento_service.repository.MantenimientoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MantenimientoService {

    private static final Logger logger = LoggerFactory.getLogger(MantenimientoService.class);

    private static final String CANCHA_SERVICE_URL = "http://localhost:7092/canchas/";

    private final MantenimientoRepository mantenimientoRepository;
    private final RestTemplate restTemplate;

    public MantenimientoService(MantenimientoRepository mantenimientoRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.restTemplate = new RestTemplate();
    }

    public Mantenimiento guardar(Mantenimiento mantenimiento) {

        logger.info("Iniciando creación de mantenimiento para cancha ID: {}", mantenimiento.getIdCancha());

        validarCanchaExiste(mantenimiento.getIdCancha());

        Mantenimiento mantenimientoGuardado = mantenimientoRepository.save(mantenimiento);

        logger.info("Mantenimiento creado correctamente con ID: {}", mantenimientoGuardado.getId());

        return mantenimientoGuardado;
    }

    public List<Mantenimiento> listar() {

        logger.info("Iniciando búsqueda de todos los mantenimientos");

        List<Mantenimiento> mantenimientos = mantenimientoRepository.findAll();

        logger.info("Búsqueda finalizada. Total de mantenimientos encontrados: {}", mantenimientos.size());

        return mantenimientos;
    }

    public Mantenimiento buscarPorId(Long id) {

        logger.info("Iniciando búsqueda de mantenimiento con ID: {}", id);

        return mantenimientoRepository.findById(id)
                .map(mantenimiento -> {
                    logger.info("Mantenimiento encontrado correctamente con ID: {}", id);
                    return mantenimiento;
                })
                .orElseThrow(() -> {
                    logger.warn("Mantenimiento no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Mantenimiento no encontrado");
                });
    }

    public Mantenimiento actualizar(Long id, Mantenimiento mantenimientoActualizado) {

        logger.info("Iniciando actualización de mantenimiento con ID: {}", id);

        Mantenimiento mantenimiento = buscarPorId(id);

        validarCanchaExiste(mantenimientoActualizado.getIdCancha());

        mantenimiento.setIdCancha(mantenimientoActualizado.getIdCancha());
        mantenimiento.setFechaInicio(mantenimientoActualizado.getFechaInicio());
        mantenimiento.setFechaFin(mantenimientoActualizado.getFechaFin());
        mantenimiento.setDescripcion(mantenimientoActualizado.getDescripcion());
        mantenimiento.setEstado(mantenimientoActualizado.getEstado());

        Mantenimiento mantenimientoGuardado = mantenimientoRepository.save(mantenimiento);

        logger.info("Mantenimiento actualizado correctamente con ID: {}", id);

        return mantenimientoGuardado;
    }

    public void eliminar(Long id) {

        logger.warn("Iniciando eliminación de mantenimiento con ID: {}", id);

        Mantenimiento mantenimiento = buscarPorId(id);

        mantenimientoRepository.delete(mantenimiento);

        logger.info("Mantenimiento eliminado correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de mantenimiento con ID: {}", id);

        boolean existe = mantenimientoRepository.existsById(id);

        logger.info("Resultado de existencia para mantenimiento con ID {}: {}", id, existe);

        return existe;
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