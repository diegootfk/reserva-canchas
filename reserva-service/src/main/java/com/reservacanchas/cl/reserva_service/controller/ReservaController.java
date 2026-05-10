package com.reservacanchas.cl.reserva_service.controller;

import com.reservacanchas.cl.reserva_service.dto.ReservaDTO;
import com.reservacanchas.cl.reserva_service.model.Reserva;
import com.reservacanchas.cl.reserva_service.service.ReservaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<Reserva> crear(@Valid @RequestBody ReservaDTO reservaDTO) {

        Reserva reserva = reservaService.guardar(reservaDTO);

        return new ResponseEntity<>(reserva, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listar() {

        return ResponseEntity.ok(reservaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> buscarPorId(@PathVariable Long id) {

        return ResponseEntity.ok(reservaService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReservaDTO reservaDTO
    ) {

        return ResponseEntity.ok(reservaService.actualizar(id, reservaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        reservaService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {

        return reservaService.existePorId(id);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Reserva>> buscarPorEstado(@PathVariable String estado) {

        return ResponseEntity.ok(reservaService.buscarPorEstado(estado));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Reserva>> buscarPorUsuario(@PathVariable Long idUsuario) {

        return ResponseEntity.ok(reservaService.buscarPorUsuario(idUsuario));
    }

    @GetMapping("/cancha/{idCancha}")
    public ResponseEntity<List<Reserva>> buscarPorCancha(@PathVariable Long idCancha) {

        return ResponseEntity.ok(reservaService.buscarPorCancha(idCancha));
    }
}