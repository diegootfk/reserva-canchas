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

    public Disponibilidad buscarPorId(Long id) {
        return disponibilidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disponibilidad no encontrada"));
    }

    public Disponibilidad actualizar(Long id, Disponibilidad disponibilidadActualizada) {
        Disponibilidad disponibilidad = buscarPorId(id);

        disponibilidad.setIdCancha(disponibilidadActualizada.getIdCancha());
        disponibilidad.setFecha(disponibilidadActualizada.getFecha());
        disponibilidad.setHoraInicio(disponibilidadActualizada.getHoraInicio());
        disponibilidad.setHoraFin(disponibilidadActualizada.getHoraFin());
        disponibilidad.setEstado(disponibilidadActualizada.getEstado());

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