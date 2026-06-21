package com.reservacanchas.cl.horario_service.service;

import com.reservacanchas.cl.horario_service.exception.BadRequestException;
import com.reservacanchas.cl.horario_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.horario_service.model.Horario;
import com.reservacanchas.cl.horario_service.repository.HorarioRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class HorarioService {

    private static final Logger logger = LoggerFactory.getLogger(HorarioService.class);

    private static final String CANCHA_SERVICE_URL = "http://localhost:7092/canchas/";

    private final HorarioRepository horarioRepository;
    private final RestTemplate restTemplate;

    public HorarioService(HorarioRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
        this.restTemplate = new RestTemplate();
    }

    public Horario guardar(Horario horario) {

        logger.info("Iniciando creación de horario para cancha ID: {}", horario.getIdCancha());

        validarDiaSemana(horario.getDiaSemana());
        validarCanchaExiste(horario.getIdCancha());

        Horario horarioGuardado = horarioRepository.save(horario);

        logger.info("Horario creado correctamente con ID: {}", horarioGuardado.getId());

        return horarioGuardado;
    }

    public List<Horario> listar() {

        logger.info("Iniciando búsqueda de todos los horarios");

        List<Horario> horarios = horarioRepository.findAll();

        logger.info("Búsqueda finalizada. Total de horarios encontrados: {}", horarios.size());

        return horarios;
    }

    public Horario buscarPorId(Long id) {

        logger.info("Iniciando búsqueda de horario con ID: {}", id);

        return horarioRepository.findById(id)
                .map(horario -> {
                    logger.info("Horario encontrado correctamente con ID: {}", id);
                    return horario;
                })
                .orElseThrow(() -> {
                    logger.warn("Horario no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Horario no encontrado");
                });
    }

    public Horario actualizar(Long id, Horario horarioActualizado) {

        logger.info("Iniciando actualización de horario con ID: {}", id);

        Horario horario = buscarPorId(id);

        validarDiaSemana(horarioActualizado.getDiaSemana());
        validarCanchaExiste(horarioActualizado.getIdCancha());

        horario.setIdCancha(horarioActualizado.getIdCancha());
        horario.setDiaSemana(horarioActualizado.getDiaSemana());
        horario.setHoraInicio(horarioActualizado.getHoraInicio());
        horario.setHoraFin(horarioActualizado.getHoraFin());
        horario.setEstado(horarioActualizado.getEstado());

        Horario horarioGuardado = horarioRepository.save(horario);

        logger.info("Horario actualizado correctamente con ID: {}", id);

        return horarioGuardado;
    }

    public void eliminar(Long id) {

        logger.warn("Iniciando eliminación de horario con ID: {}", id);

        Horario horario = buscarPorId(id);

        horarioRepository.delete(horario);

        logger.info("Horario eliminado correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de horario con ID: {}", id);

        boolean existe = horarioRepository.existsById(id);

        logger.info("Resultado de existencia para horario con ID {}: {}", id, existe);

        return existe;
    }

    private void validarDiaSemana(String diaSemana) {

        logger.debug("Validando día de la semana del horario");

        if (diaSemana == null || diaSemana.isBlank()) {
            logger.warn("Validación fallida: el día de la semana está vacío o es nulo");
            throw new BadRequestException("El día de la semana es obligatorio");
        }

        logger.debug("Día de la semana validado correctamente");
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