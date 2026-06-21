package com.reservacanchas.cl.mantenimiento_service.service;

import com.reservacanchas.cl.mantenimiento_service.dto.MantenimientoDTO;
import com.reservacanchas.cl.mantenimiento_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.mantenimiento_service.model.Mantenimiento;
import com.reservacanchas.cl.mantenimiento_service.repository.MantenimientoRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MantenimientoService {

    private final MantenimientoRepository mantenimientoRepository;
    private final RestTemplate restTemplate;

    public MantenimientoService(MantenimientoRepository mantenimientoRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.restTemplate = new RestTemplate();
    }

    public Mantenimiento guardar(MantenimientoDTO mantenimientoDTO) {

        Boolean canchaExiste = restTemplate.getForObject(
                "http://localhost:7092/canchas/" + mantenimientoDTO.getIdCancha() + "/exists",
                Boolean.class
        );

        if (canchaExiste == null || !canchaExiste) {
            throw new ResourceNotFoundException("La cancha no existe");
        }

        Mantenimiento mantenimiento = new Mantenimiento();

        mantenimiento.setIdCancha(mantenimientoDTO.getIdCancha());
        mantenimiento.setFechaInicio(mantenimientoDTO.getFechaInicio());
        mantenimiento.setFechaFin(mantenimientoDTO.getFechaFin());
        mantenimiento.setDescripcion(mantenimientoDTO.getDescripcion());
        mantenimiento.setEstado(mantenimientoDTO.getEstado());

        return mantenimientoRepository.save(mantenimiento);
    }

    public List<Mantenimiento> listar() {
        return mantenimientoRepository.findAll();
    }

    public Mantenimiento buscarPorId(Long id) {
        return mantenimientoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Mantenimiento no encontrado"));
    }

    public Mantenimiento actualizar(Long id, MantenimientoDTO mantenimientoDTO) {

        Mantenimiento mantenimiento = buscarPorId(id);

        mantenimiento.setIdCancha(mantenimientoDTO.getIdCancha());
        mantenimiento.setFechaInicio(mantenimientoDTO.getFechaInicio());
        mantenimiento.setFechaFin(mantenimientoDTO.getFechaFin());
        mantenimiento.setDescripcion(mantenimientoDTO.getDescripcion());
        mantenimiento.setEstado(mantenimientoDTO.getEstado());

        return mantenimientoRepository.save(mantenimiento);
    }

    public void eliminar(Long id) {

        Mantenimiento mantenimiento = buscarPorId(id);

        mantenimientoRepository.delete(mantenimiento);
    }

    public boolean existePorId(Long id) {
        return mantenimientoRepository.existsById(id);
    }
}