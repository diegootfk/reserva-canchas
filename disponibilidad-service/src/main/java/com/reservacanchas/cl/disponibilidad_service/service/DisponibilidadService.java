package com.reservacanchas.cl.disponibilidad_service.service;

import com.reservacanchas.cl.disponibilidad_service.dto.DisponibilidadDTO;
import com.reservacanchas.cl.disponibilidad_service.exception.BadRequestException;
import com.reservacanchas.cl.disponibilidad_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.disponibilidad_service.model.Disponibilidad;
import com.reservacanchas.cl.disponibilidad_service.repository.DisponibilidadRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class DisponibilidadService {

    private final DisponibilidadRepository disponibilidadRepository;
    private final RestTemplate restTemplate;

    public DisponibilidadService(DisponibilidadRepository disponibilidadRepository) {
        this.disponibilidadRepository = disponibilidadRepository;
        this.restTemplate = new RestTemplate();
    }

    public Disponibilidad guardar(DisponibilidadDTO disponibilidadDTO) {

        if (disponibilidadDTO.getFecha() == null
                || disponibilidadDTO.getFecha().isBlank()) {

            throw new BadRequestException("La fecha es obligatoria");
        }

        Boolean canchaExiste = restTemplate.getForObject(
                "http://localhost:7092/canchas/"
                        + disponibilidadDTO.getIdCancha()
                        + "/exists",
                Boolean.class
        );

        if (canchaExiste == null || !canchaExiste) {
            throw new ResourceNotFoundException("La cancha no existe");
        }

        Disponibilidad disponibilidad = new Disponibilidad();

        disponibilidad.setIdCancha(disponibilidadDTO.getIdCancha());
        disponibilidad.setFecha(disponibilidadDTO.getFecha());
        disponibilidad.setHoraInicio(disponibilidadDTO.getHoraInicio());
        disponibilidad.setHoraFin(disponibilidadDTO.getHoraFin());
        disponibilidad.setEstado(disponibilidadDTO.getEstado());

        return disponibilidadRepository.save(disponibilidad);
    }

    public List<Disponibilidad> listar() {
        return disponibilidadRepository.findAll();
    }

    public Disponibilidad buscarPorId(Long id) {
        return disponibilidadRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Disponibilidad no encontrada"));
    }

    public Disponibilidad actualizar(
            Long id,
            DisponibilidadDTO disponibilidadDTO) {

        Disponibilidad disponibilidad = buscarPorId(id);

        disponibilidad.setIdCancha(disponibilidadDTO.getIdCancha());
        disponibilidad.setFecha(disponibilidadDTO.getFecha());
        disponibilidad.setHoraInicio(disponibilidadDTO.getHoraInicio());
        disponibilidad.setHoraFin(disponibilidadDTO.getHoraFin());
        disponibilidad.setEstado(disponibilidadDTO.getEstado());

        return disponibilidadRepository.save(disponibilidad);
    }

    public void eliminar(Long id) {

        Disponibilidad disponibilidad = buscarPorId(id);

        disponibilidadRepository.delete(disponibilidad);
    }

    public boolean existePorId(Long id) {
        return disponibilidadRepository.existsById(id);
    }
}