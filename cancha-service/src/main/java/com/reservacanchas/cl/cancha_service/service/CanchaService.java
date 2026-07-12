package com.reservacanchas.cl.cancha_service.service;

import com.reservacanchas.cl.cancha_service.dto.CanchaDTO;
import com.reservacanchas.cl.cancha_service.exception.BadRequestException;
import com.reservacanchas.cl.cancha_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.cancha_service.model.Cancha;
import com.reservacanchas.cl.cancha_service.repository.CanchaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CanchaService {

    private static final Logger logger =
            LoggerFactory.getLogger(CanchaService.class);

    private final CanchaRepository canchaRepository;

    public CanchaService(CanchaRepository canchaRepository) {
        this.canchaRepository = canchaRepository;
    }

    public Cancha guardar(CanchaDTO canchaDTO) {

        logger.info("Intentando crear cancha con nombre: {}",
                canchaDTO.getNombre());

        if (canchaDTO.getNombre() == null || canchaDTO.getNombre().isBlank()) {

            logger.error("Error al crear cancha: nombre vacío");

            throw new BadRequestException(
                    "El nombre de la cancha es obligatorio");
        }

        if (canchaDTO.getPrecioHora() == null
                || canchaDTO.getPrecioHora() <= 0) {

            logger.error(
                    "Error al crear cancha: precio por hora inválido ({})",
                    canchaDTO.getPrecioHora()
            );

            throw new BadRequestException(
                    "El precio por hora debe ser mayor a 0");
        }

        Cancha cancha = new Cancha();

        cancha.setNombre(canchaDTO.getNombre());
        cancha.setTipoCancha(canchaDTO.getTipoCancha());
        cancha.setPrecioHora(canchaDTO.getPrecioHora());
        cancha.setCapacidad(canchaDTO.getCapacidad());
        cancha.setEstado(canchaDTO.getEstado());

        Cancha canchaGuardada = canchaRepository.save(cancha);

        logger.info("Cancha creada correctamente con ID: {}",
                canchaGuardada.getId());

        return canchaGuardada;
    }

    public List<Cancha> listar() {

        logger.info("Listando todas las canchas");

        return canchaRepository.findAll();
    }

    public Cancha buscarPorId(Long id) {

        logger.info("Buscando cancha con ID: {}", id);

        return canchaRepository.findById(id)
                .orElseThrow(() -> {

                    logger.warn("Cancha con ID {} no encontrada", id);

                    return new ResourceNotFoundException(
                            "Cancha no encontrada");
                });
    }

    public Cancha actualizar(Long id, CanchaDTO canchaDTO) {

        logger.info("Actualizando cancha con ID: {}", id);

        Cancha cancha = buscarPorId(id);

        cancha.setNombre(canchaDTO.getNombre());
        cancha.setTipoCancha(canchaDTO.getTipoCancha());
        cancha.setPrecioHora(canchaDTO.getPrecioHora());
        cancha.setCapacidad(canchaDTO.getCapacidad());
        cancha.setEstado(canchaDTO.getEstado());

        Cancha canchaActualizada = canchaRepository.save(cancha);

        logger.info("Cancha actualizada correctamente con ID: {}", id);

        return canchaActualizada;
    }

    public void eliminar(Long id) {

        logger.info("Eliminando cancha con ID: {}", id);

        Cancha cancha = buscarPorId(id);

        canchaRepository.delete(cancha);

        logger.info("Cancha eliminada correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de cancha con ID: {}", id);

        return canchaRepository.existsById(id);
    }
}