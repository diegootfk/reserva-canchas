package com.reservacanchas.cl.sede_service.service;

import com.reservacanchas.cl.sede_service.exception.BadRequestException;
import com.reservacanchas.cl.sede_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.sede_service.model.Sede;
import com.reservacanchas.cl.sede_service.repository.SedeRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SedeService {

    private final SedeRepository sedeRepository;

    public SedeService(SedeRepository sedeRepository) {
        this.sedeRepository = sedeRepository;
    }

    public Sede guardar(Sede sede) {

        if (sede.getNombre() == null || sede.getNombre().isBlank()) {
            throw new BadRequestException("El nombre de la sede es obligatorio");
        }

        return sedeRepository.save(sede);
    }

    public List<Sede> listar() {
        return sedeRepository.findAll();
    }

    public Sede buscarPorId(Long id) {
        return sedeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sede no encontrada"));
    }

    public Sede actualizar(Long id, Sede sedeActualizada) {

        Sede sede = buscarPorId(id);

        sede.setNombre(sedeActualizada.getNombre());
        sede.setDireccion(sedeActualizada.getDireccion());
        sede.setComuna(sedeActualizada.getComuna());
        sede.setTelefono(sedeActualizada.getTelefono());
        sede.setEstado(sedeActualizada.getEstado());

        return sedeRepository.save(sede);
    }

    public void eliminar(Long id) {

        Sede sede = buscarPorId(id);

        sedeRepository.delete(sede);
    }

    public boolean existePorId(Long id) {
        return sedeRepository.existsById(id);
    }
}