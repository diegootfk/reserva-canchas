package com.reservacanchas.cl.cancha_service.controller;

import com.reservacanchas.cl.cancha_service.dto.CanchaDTO;
import com.reservacanchas.cl.cancha_service.model.Cancha;
import com.reservacanchas.cl.cancha_service.service.CanchaService;
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
    public Cancha crear(@RequestBody CanchaDTO canchaDTO){
        return canchaService.guardar(canchaDTO);
    }

    @GetMapping
    public List<Cancha> listar(){
        return canchaService.listar();
    }

    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id){
        return canchaService.existePorId(id);
    }
}