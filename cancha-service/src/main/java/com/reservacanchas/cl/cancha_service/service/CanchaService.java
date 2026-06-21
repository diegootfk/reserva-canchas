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

    private static final Logger logger = LoggerFactory.getLogger(CanchaService.class);

    private final CanchaRepository canchaRepository;

    public CanchaService(CanchaRepository canchaRepository) {
        this.canchaRepository = canchaRepository;
    }

    public Cancha guardar(CanchaDTO canchaDTO) {

        logger.info("Iniciando creación de una nueva cancha");

        validarNombre(canchaDTO.getNombre());

        Cancha cancha = new Cancha();

        cancha.setNombre(canchaDTO.getNombre());
        cancha.setTipoCancha(canchaDTO.getTipoCancha());
        cancha.setPrecioHora(canchaDTO.getPrecioHora());
        cancha.setCapacidad(canchaDTO.getCapacidad());
        cancha.setEstado(canchaDTO.getEstado());

        Cancha canchaGuardada = canchaRepository.save(cancha);

        logger.info("Cancha creada correctamente con ID: {}", canchaGuardada.getId());

        return canchaGuardada;
    }

    public List<Cancha> listar() {

        logger.info("Iniciando búsqueda de todas las canchas");

        List<Cancha> canchas = canchaRepository.findAll();

        logger.info("Búsqueda finalizada. Total de canchas encontradas: {}", canchas.size());

        return canchas;
    }

    public Cancha buscarPorId(Long id) {

        logger.info("Iniciando búsqueda de cancha con ID: {}", id);

        return canchaRepository.findById(id)
                .map(cancha -> {
                    logger.info("Cancha encontrada correctamente con ID: {}", id);
                    return cancha;
                })
                .orElseThrow(() -> {
                    logger.warn("Cancha no encontrada con ID: {}", id);
                    return new ResourceNotFoundException("Cancha no encontrada");
                });
    }

    public Cancha actualizar(Long id, CanchaDTO canchaDTO) {

        logger.info("Iniciando actualización de cancha con ID: {}", id);

        validarNombre(canchaDTO.getNombre());

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

        logger.warn("Iniciando eliminación de cancha con ID: {}", id);

        Cancha cancha = buscarPorId(id);

        canchaRepository.delete(cancha);

        logger.info("Cancha eliminada correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de cancha con ID: {}", id);

        boolean existe = canchaRepository.existsById(id);

        logger.info("Resultado de existencia para cancha con ID {}: {}", id, existe);

        return existe;
    }

    private void validarNombre(String nombre) {

        logger.debug("Validando nombre de la cancha");

        if (nombre == null || nombre.isBlank()) {
            logger.warn("Validación fallida: el nombre de la cancha está vacío o es nulo");
            throw new BadRequestException("El nombre de la cancha es obligatorio");
        }

        logger.debug("Nombre de cancha validado correctamente");
    }
}