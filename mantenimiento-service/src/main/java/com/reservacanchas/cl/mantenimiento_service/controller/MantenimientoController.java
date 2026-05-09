package com.reservacanchas.cl.mantenimiento_service.controller;

import com.reservacanchas.cl.mantenimiento_service.model.Mantenimiento;
import com.reservacanchas.cl.mantenimiento_service.service.MantenimientoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Mantenimiento> crear(@RequestBody Mantenimiento mantenimiento) {
        return new ResponseEntity<>(mantenimientoService.guardar(mantenimiento), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Mantenimiento>> listar() {
        return ResponseEntity.ok(mantenimientoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mantenimiento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mantenimientoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Mantenimiento> actualizar(@PathVariable Long id, @RequestBody Mantenimiento mantenimiento) {
        return ResponseEntity.ok(mantenimientoService.actualizar(id, mantenimiento));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mantenimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {
        return mantenimientoService.existePorId(id);
    }
}