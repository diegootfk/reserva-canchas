package com.reservacanchas.cl.sede_service.service;

import com.reservacanchas.cl.sede_service.dto.SedeDTO;
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

    private static final Logger logger =
            LoggerFactory.getLogger(SedeService.class);

    private final SedeRepository sedeRepository;

    public SedeService(SedeRepository sedeRepository) {
        this.sedeRepository = sedeRepository;
    }

    public Sede guardar(SedeDTO sedeDTO) {

        logger.info("Intentando guardar sede con nombre: {}",
                sedeDTO.getNombre());

        if (sedeDTO.getNombre() == null || sedeDTO.getNombre().isBlank()) {

            logger.error("Error al guardar sede: nombre vacío");

            throw new BadRequestException("El nombre de la sede es obligatorio");
        }

        Sede sede = new Sede();

        sede.setNombre(sedeDTO.getNombre());
        sede.setDireccion(sedeDTO.getDireccion());
        sede.setComuna(sedeDTO.getComuna());
        sede.setTelefono(sedeDTO.getTelefono());
        sede.setEstado(sedeDTO.getEstado());

        Sede sedeGuardada = sedeRepository.save(sede);

        logger.info("Sede creada correctamente con ID: {}",
                sedeGuardada.getId());

        return sedeGuardada;
    }

    public List<Sede> listar() {

        logger.info("Listando todas las sedes");

        return sedeRepository.findAll();
    }

    public Sede buscarPorId(Long id) {

        logger.info("Buscando sede con ID: {}", id);

        return sedeRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Sede no encontrada con ID: {}", id);

                    return new ResourceNotFoundException(
                            "Sede no encontrada");
                });
    }

    public Sede actualizar(Long id, SedeDTO sedeDTO) {

        logger.info("Actualizando sede con ID: {}", id);

        Sede sede = buscarPorId(id);

        sede.setNombre(sedeDTO.getNombre());
        sede.setDireccion(sedeDTO.getDireccion());
        sede.setComuna(sedeDTO.getComuna());
        sede.setTelefono(sedeDTO.getTelefono());
        sede.setEstado(sedeDTO.getEstado());

        Sede sedeActualizada = sedeRepository.save(sede);

        logger.info("Sede actualizada correctamente con ID: {}", id);

        return sedeActualizada;
    }

    public void eliminar(Long id) {

        logger.info("Eliminando sede con ID: {}", id);

        Sede sede = buscarPorId(id);

        sedeRepository.delete(sede);

        logger.info("Sede eliminada correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de sede con ID: {}", id);

        boolean existe = sedeRepository.existsById(id);

        logger.info("Resultado existencia sede {}: {}", id, existe);

        return existe;
    }
}