package com.reservacanchas.cl.mantenimiento_service.controller;

import com.reservacanchas.cl.mantenimiento_service.model.Mantenimiento;
import com.reservacanchas.cl.mantenimiento_service.service.MantenimientoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mantenimientos")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    public MantenimientoController(MantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    @PostMapping
    public Mantenimiento crear(@RequestBody Mantenimiento mantenimiento) {
        return mantenimientoService.guardar(mantenimiento);
    }

    @GetMapping
    public List<Mantenimiento> listar() {
        return mantenimientoService.listar();
    }

    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {
        return mantenimientoService.existePorId(id);
    }
}