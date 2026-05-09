package com.reservacanchas.cl.horario_service.controller;

import com.reservacanchas.cl.horario_service.model.Horario;
import com.reservacanchas.cl.horario_service.service.HorarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/horarios")
public class HorarioController {

    private final HorarioService horarioService;

    public HorarioController(HorarioService horarioService) {
        this.horarioService = horarioService;
    }

    @PostMapping
    public ResponseEntity<Horario> crear(@RequestBody Horario horario) {
        return new ResponseEntity<>(horarioService.guardar(horario), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Horario>> listar() {
        return ResponseEntity.ok(horarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Horario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Horario> actualizar(@PathVariable Long id, @RequestBody Horario horario) {
        return ResponseEntity.ok(horarioService.actualizar(id, horario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        horarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {
        return horarioService.existePorId(id);
    }
}