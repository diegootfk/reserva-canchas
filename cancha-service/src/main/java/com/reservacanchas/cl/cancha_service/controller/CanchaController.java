package com.reservacanchas.cl.cancha_service.controller;

import com.reservacanchas.cl.cancha_service.dto.CanchaDTO;
import com.reservacanchas.cl.cancha_service.model.Cancha;
import com.reservacanchas.cl.cancha_service.service.CanchaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/canchas")
public class CanchaController {

    private final CanchaService canchaService;

    public CanchaController(CanchaService canchaService) {
        this.canchaService = canchaService;
    }

    @PostMapping
    public ResponseEntity<Cancha> crear(@RequestBody CanchaDTO canchaDTO) {
        Cancha cancha = canchaService.guardar(canchaDTO);
        return new ResponseEntity<>(cancha, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Cancha>> listar() {
        return ResponseEntity.ok(canchaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cancha> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(canchaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cancha> actualizar(@PathVariable Long id, @RequestBody CanchaDTO canchaDTO) {
        return ResponseEntity.ok(canchaService.actualizar(id, canchaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        canchaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {
        return canchaService.existePorId(id);
    }
}