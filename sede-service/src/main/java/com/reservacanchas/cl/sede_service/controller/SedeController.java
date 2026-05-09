package com.reservacanchas.cl.sede_service.controller;

import com.reservacanchas.cl.sede_service.model.Sede;
import com.reservacanchas.cl.sede_service.service.SedeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sedes")
public class SedeController {

    private final SedeService sedeService;

    public SedeController(SedeService sedeService) {
        this.sedeService = sedeService;
    }

    @PostMapping
    public Sede crear(@RequestBody Sede sede) {
        return sedeService.guardar(sede);
    }

    @GetMapping
    public List<Sede> listar() {
        return sedeService.listar();
    }

    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {
        return sedeService.existePorId(id);
    }
}