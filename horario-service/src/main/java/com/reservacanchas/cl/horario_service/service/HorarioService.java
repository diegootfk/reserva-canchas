package com.reservacanchas.cl.horario_service.service;

import com.reservacanchas.cl.horario_service.dto.HorarioDTO;
import com.reservacanchas.cl.horario_service.exception.BadRequestException;
import com.reservacanchas.cl.horario_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.horario_service.model.Horario;
import com.reservacanchas.cl.horario_service.repository.HorarioRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class HorarioService {

    private static final Logger logger =
            LoggerFactory.getLogger(HorarioService.class);

    private final HorarioRepository horarioRepository;
    private final WebClient.Builder webClientBuilder;

    public HorarioService(
            HorarioRepository horarioRepository,
            WebClient.Builder webClientBuilder) {

        this.horarioRepository = horarioRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public Horario guardar(HorarioDTO horarioDTO) {

        logger.info(
                "Iniciando creación de horario para cancha {}",
                horarioDTO.getIdCancha()
        );

        if (horarioDTO.getDiaSemana() == null
                || horarioDTO.getDiaSemana().isBlank()) {

            logger.warn(
                    "No se pudo crear horario: día de la semana vacío"
            );

            throw new BadRequestException(
                    "El día de la semana es obligatorio");
        }

        if (horarioDTO.getHoraInicio() != null
                && horarioDTO.getHoraFin() != null
                && horarioDTO.getHoraFin().compareTo(
                        horarioDTO.getHoraInicio()) <= 0) {

            logger.warn(
                    "No se pudo crear horario: horaFin {} no es posterior a horaInicio {}",
                    horarioDTO.getHoraFin(),
                    horarioDTO.getHoraInicio()
            );

            throw new BadRequestException(
                    "La hora de fin debe ser posterior a la hora de inicio");
        }

        Boolean canchaExiste = webClientBuilder.build()
                .get()
                .uri("http://localhost:7092/canchas/"
                        + horarioDTO.getIdCancha()
                        + "/exists")
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (canchaExiste == null || !canchaExiste) {

            logger.warn(
                    "No se pudo crear horario. Cancha {} no existe",
                    horarioDTO.getIdCancha()
            );

            throw new ResourceNotFoundException(
                    "La cancha no existe"
            );
        }

        Horario horario = new Horario();

        horario.setIdCancha(horarioDTO.getIdCancha());
        horario.setDiaSemana(horarioDTO.getDiaSemana());
        horario.setHoraInicio(horarioDTO.getHoraInicio());
        horario.setHoraFin(horarioDTO.getHoraFin());
        horario.setEstado(horarioDTO.getEstado());

        Horario horarioGuardado =
                horarioRepository.save(horario);

        logger.info(
                "Horario creado correctamente con ID {}",
                horarioGuardado.getId()
        );

        return horarioGuardado;
    }

    public List<Horario> listar() {

        logger.info("Listando todos los horarios");

        List<Horario> horarios = horarioRepository.findAll();

        logger.info(
                "Se encontraron {} horarios",
                horarios.size()
        );

        return horarios;
    }

    public Horario buscarPorId(Long id) {

        logger.info(
                "Buscando horario con ID {}",
                id
        );

        return horarioRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error(
                            "Horario con ID {} no encontrado",
                            id
                    );

                    return new ResourceNotFoundException(
                            "Horario no encontrado");
                });
    }

    public Horario actualizar(
            Long id,
            HorarioDTO horarioDTO) {

        logger.info(
                "Actualizando horario con ID {}",
                id
        );

        Horario horario = buscarPorId(id);

        horario.setIdCancha(horarioDTO.getIdCancha());
        horario.setDiaSemana(horarioDTO.getDiaSemana());
        horario.setHoraInicio(horarioDTO.getHoraInicio());
        horario.setHoraFin(horarioDTO.getHoraFin());
        horario.setEstado(horarioDTO.getEstado());

        Horario horarioActualizado =
                horarioRepository.save(horario);

        logger.info(
                "Horario actualizado correctamente con ID {}",
                id
        );

        return horarioActualizado;
    }

    public void eliminar(Long id) {

        logger.info(
                "Eliminando horario con ID {}",
                id
        );

        Horario horario = buscarPorId(id);

        horarioRepository.delete(horario);

        logger.info(
                "Horario eliminado correctamente con ID {}",
                id
        );
    }

    public boolean existePorId(Long id) {

        logger.info(
                "Verificando existencia de horario con ID {}",
                id
        );

        boolean existe = horarioRepository.existsById(id);

        logger.info(
                "Resultado existencia horario {}: {}",
                id,
                existe
        );

        return existe;
    }
}