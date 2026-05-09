package com.reservacanchas.cl.disponibilidad_service.service;

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

    public Disponibilidad guardar(Disponibilidad disponibilidad) {

        Boolean canchaExiste = restTemplate.getForObject(
                "http://localhost:9092/canchas/" + disponibilidad.getIdCancha() + "/exists",
                Boolean.class
        );

        if (canchaExiste == null || !canchaExiste) {
            throw new RuntimeException("La cancha no existe");
        }

        return disponibilidadRepository.save(disponibilidad);
    }

    public List<Disponibilidad> listar() {
        return disponibilidadRepository.findAll();
    }

    public boolean existePorId(Long id) {
        return disponibilidadRepository.existsById(id);
    }
}