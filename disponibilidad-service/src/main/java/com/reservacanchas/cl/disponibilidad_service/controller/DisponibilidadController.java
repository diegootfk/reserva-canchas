package com.reservacanchas.cl.disponibilidad_service.controller;

import com.reservacanchas.cl.disponibilidad_service.model.Disponibilidad;
import com.reservacanchas.cl.disponibilidad_service.service.DisponibilidadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Disponibilidad> crear(@RequestBody Disponibilidad disponibilidad) {
        return new ResponseEntity<>(disponibilidadService.guardar(disponibilidad), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Disponibilidad>> listar() {
        return ResponseEntity.ok(disponibilidadService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Disponibilidad> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(disponibilidadService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Disponibilidad> actualizar(@PathVariable Long id, @RequestBody Disponibilidad disponibilidad) {
        return ResponseEntity.ok(disponibilidadService.actualizar(id, disponibilidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        disponibilidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {
        return disponibilidadService.existePorId(id);
    }
}