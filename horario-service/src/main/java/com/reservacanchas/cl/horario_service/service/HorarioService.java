package com.reservacanchas.cl.horario_service.service;

import com.reservacanchas.cl.horario_service.dto.HorarioDTO;
import com.reservacanchas.cl.horario_service.exception.BadRequestException;
import com.reservacanchas.cl.horario_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.horario_service.model.Horario;
import com.reservacanchas.cl.horario_service.repository.HorarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final RestTemplate restTemplate;

    public HorarioService(HorarioRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
        this.restTemplate = new RestTemplate();
    }

    public Horario guardar(HorarioDTO horarioDTO) {

        if (horarioDTO.getDiaSemana() == null
                || horarioDTO.getDiaSemana().isBlank()) {

            throw new BadRequestException(
                    "El día de la semana es obligatorio");
        }

        Boolean canchaExiste = restTemplate.getForObject(
                "http://localhost:7092/canchas/"
                        + horarioDTO.getIdCancha()
                        + "/exists",
                Boolean.class
        );

        if (canchaExiste == null || !canchaExiste) {
            throw new ResourceNotFoundException("La cancha no existe");
        }

        Horario horario = new Horario();

        horario.setIdCancha(horarioDTO.getIdCancha());
        horario.setDiaSemana(horarioDTO.getDiaSemana());
        horario.setHoraInicio(horarioDTO.getHoraInicio());
        horario.setHoraFin(horarioDTO.getHoraFin());
        horario.setEstado(horarioDTO.getEstado());

        return horarioRepository.save(horario);
    }

    public List<Horario> listar() {
        return horarioRepository.findAll();
    }

    public Horario buscarPorId(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Horario no encontrado"));
    }

    public Horario actualizar(
            Long id,
            HorarioDTO horarioDTO) {

        Horario horario = buscarPorId(id);

        horario.setIdCancha(horarioDTO.getIdCancha());
        horario.setDiaSemana(horarioDTO.getDiaSemana());
        horario.setHoraInicio(horarioDTO.getHoraInicio());
        horario.setHoraFin(horarioDTO.getHoraFin());
        horario.setEstado(horarioDTO.getEstado());

        return horarioRepository.save(horario);
    }

    public void eliminar(Long id) {

        Horario horario = buscarPorId(id);

        horarioRepository.delete(horario);
    }

    public boolean existePorId(Long id) {
        return horarioRepository.existsById(id);
    }
}