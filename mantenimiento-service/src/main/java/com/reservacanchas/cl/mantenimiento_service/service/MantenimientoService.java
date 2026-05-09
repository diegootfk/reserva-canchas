package com.reservacanchas.cl.mantenimiento_service.service;

import com.reservacanchas.cl.mantenimiento_service.exception.RecursoNoEncontradoException;
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

    public Mantenimiento guardar(Mantenimiento mantenimiento) {

        Boolean canchaExiste = restTemplate.getForObject(
                "http://localhost:9092/canchas/" + mantenimiento.getIdCancha() + "/exists",
                Boolean.class
        );

        if (canchaExiste == null || !canchaExiste) {
            throw new RecursoNoEncontradoException("La cancha no existe");
        }

        return mantenimientoRepository.save(mantenimiento);
    }

    public List<Mantenimiento> listar() {
        return mantenimientoRepository.findAll();
    }

    public Mantenimiento buscarPorId(Long id) {
        return mantenimientoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mantenimiento no encontrado"));
    }

    public Mantenimiento actualizar(Long id, Mantenimiento mantenimientoActualizado) {
        Mantenimiento mantenimiento = buscarPorId(id);

        mantenimiento.setIdCancha(mantenimientoActualizado.getIdCancha());
        mantenimiento.setFechaInicio(mantenimientoActualizado.getFechaInicio());
        mantenimiento.setFechaFin(mantenimientoActualizado.getFechaFin());
        mantenimiento.setDescripcion(mantenimientoActualizado.getDescripcion());
        mantenimiento.setEstado(mantenimientoActualizado.getEstado());

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