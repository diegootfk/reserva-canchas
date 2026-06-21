package com.reservacanchas.cl.sede_service.service;

import com.reservacanchas.cl.sede_service.exception.BadRequestException;
import com.reservacanchas.cl.sede_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.sede_service.model.Sede;
import com.reservacanchas.cl.sede_service.repository.SedeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SedeService {

    private static final Logger logger = LoggerFactory.getLogger(SedeService.class);

    private final SedeRepository sedeRepository;

    public SedeService(SedeRepository sedeRepository) {
        this.sedeRepository = sedeRepository;
    }

    public Sede guardar(Sede sede) {

        logger.info("Iniciando proceso para guardar una nueva sede");

        validarNombre(sede.getNombre());

        Sede sedeGuardada = sedeRepository.save(sede);

        logger.info("Sede creada correctamente con ID: {}", sedeGuardada.getId());

        return sedeGuardada;
    }

    public List<Sede> listar() {

        logger.info("Iniciando búsqueda de todas las sedes");

        List<Sede> sedes = sedeRepository.findAll();

        logger.info("Búsqueda finalizada. Total de sedes encontradas: {}", sedes.size());

        return sedes;
    }

    public Sede buscarPorId(Long id) {

        logger.info("Iniciando búsqueda de sede con ID: {}", id);

        return sedeRepository.findById(id)
                .map(sede -> {
                    logger.info("Sede encontrada correctamente con ID: {}", id);
                    return sede;
                })
                .orElseThrow(() -> {
                    logger.warn("Sede no encontrada con ID: {}", id);
                    return new ResourceNotFoundException("Sede no encontrada");
                });
    }

    public Sede actualizar(Long id, Sede sedeActualizada) {

        logger.info("Iniciando actualización de sede con ID: {}", id);

        validarNombre(sedeActualizada.getNombre());

        Sede sede = buscarPorId(id);

        sede.setNombre(sedeActualizada.getNombre());
        sede.setDireccion(sedeActualizada.getDireccion());
        sede.setComuna(sedeActualizada.getComuna());
        sede.setTelefono(sedeActualizada.getTelefono());
        sede.setEstado(sedeActualizada.getEstado());

        Sede sedeGuardada = sedeRepository.save(sede);

        logger.info("Sede actualizada correctamente con ID: {}", id);

        return sedeGuardada;
    }

    public void eliminar(Long id) {

        logger.warn("Iniciando eliminación de sede con ID: {}", id);

        Sede sede = buscarPorId(id);

        sedeRepository.delete(sede);

        logger.info("Sede eliminada correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de sede con ID: {}", id);

        boolean existe = sedeRepository.existsById(id);

        logger.info("Resultado de existencia para sede con ID {}: {}", id, existe);

        return existe;
    }

    private void validarNombre(String nombre) {

        logger.debug("Validando nombre de la sede");

        if (nombre == null || nombre.isBlank()) {
            logger.warn("Validación fallida: el nombre de la sede está vacío o es nulo");
            throw new BadRequestException("El nombre de la sede es obligatorio");
        }

        logger.debug("Nombre de sede validado correctamente");
    }
}