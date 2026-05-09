package com.reservacanchas.cl.disponibilidad_service.controller;

import com.reservacanchas.cl.disponibilidad_service.model.Disponibilidad;
import com.reservacanchas.cl.disponibilidad_service.service.DisponibilidadService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disponibilidades")
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    public DisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    @PostMapping
    public Disponibilidad crear(@RequestBody Disponibilidad disponibilidad) {
        return disponibilidadService.guardar(disponibilidad);
    }

    @GetMapping
    public List<Disponibilidad> listar() {
        return disponibilidadService.listar();
    }

    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {
        return disponibilidadService.existePorId(id);
    }
}