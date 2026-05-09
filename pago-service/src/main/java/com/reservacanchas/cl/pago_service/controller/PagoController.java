package com.reservacanchas.cl.pago_service.controller;

import com.reservacanchas.cl.pago_service.dto.PagoDTO;
import com.reservacanchas.cl.pago_service.model.Pago;
import com.reservacanchas.cl.pago_service.service.PagoService;
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
    public Pago crear(@RequestBody PagoDTO pagoDTO) {
        return pagoService.guardar(pagoDTO);
    }

    @GetMapping
    public List<Pago> listar() {
        return pagoService.listar();
    }

    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {
        return pagoService.existePorId(id);
    }
}