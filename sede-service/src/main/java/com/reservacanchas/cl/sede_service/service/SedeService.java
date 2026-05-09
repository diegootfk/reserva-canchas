package com.reservacanchas.cl.sede_service.service;

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
        return sedeRepository.save(sede);
    }

    public List<Sede> listar() {
        return sedeRepository.findAll();
    }

    public boolean existePorId(Long id) {
        return sedeRepository.existsById(id);
    }
}