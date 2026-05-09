package com.reservacanchas.cl.resena_service.controller;

import com.reservacanchas.cl.resena_service.model.Resena;
import com.reservacanchas.cl.resena_service.service.ResenaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resenas")
public class ResenaController {

    private final ResenaService resenaService;

    public ResenaController(ResenaService resenaService) {
        this.resenaService = resenaService;
    }

    @PostMapping
    public Resena crear(@RequestBody Resena resena) {
        return resenaService.guardar(resena);
    }

    @GetMapping
    public List<Resena> listar() {
        return resenaService.listar();
    }

    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id) {
        return resenaService.existePorId(id);
    }
}