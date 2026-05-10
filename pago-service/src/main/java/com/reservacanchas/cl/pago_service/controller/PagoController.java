package com.reservacanchas.cl.pago_service.controller;

import com.reservacanchas.cl.pago_service.dto.PagoDTO;
import com.reservacanchas.cl.pago_service.model.Pago;
import com.reservacanchas.cl.pago_service.service.PagoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<Pago> crear(@Valid @RequestBody PagoDTO pagoDTO) {
        Pago pago = pagoService.guardar(pagoDTO);
        return new ResponseEntity<>(pago, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Pago>> listar() {
        return ResponseEntity.ok(pagoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizar(@PathVariable Long id,
                                           @Valid @RequestBody PagoDTO pagoDTO) {
        return ResponseEntity.ok(pagoService.actualizar(id, pagoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {
        return pagoService.existePorId(id);
    }

    @GetMapping("/metodo/{metodoPago}")
    public ResponseEntity<List<Pago>> buscarPorMetodoPago(@PathVariable String metodoPago) {
        return ResponseEntity.ok(pagoService.buscarPorMetodoPago(metodoPago));
    }

    @GetMapping("/estado/{estadoPago}")
    public ResponseEntity<List<Pago>> buscarPorEstadoPago(@PathVariable String estadoPago) {
        return ResponseEntity.ok(pagoService.buscarPorEstadoPago(estadoPago));
    }

    @GetMapping("/reserva/{idReserva}")
    public ResponseEntity<List<Pago>> buscarPorReserva(@PathVariable Long idReserva) {
        return ResponseEntity.ok(pagoService.buscarPorReserva(idReserva));
    }
}