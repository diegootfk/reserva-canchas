package com.reservacanchas.cl.usuario_service.controller;

import com.reservacanchas.cl.usuario_service.dto.UsuarioDTO;
import com.reservacanchas.cl.usuario_service.model.Usuario;
import com.reservacanchas.cl.usuario_service.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Usuario crear(@RequestBody UsuarioDTO usuarioDTO){
        return usuarioService.guardar(usuarioDTO);
    }

    @GetMapping
    public List<Usuario> listar(){
        return usuarioService.listar();
    }

    @GetMapping("/{id}/exists")
    public boolean existe(@PathVariable Long id){
        return usuarioService.existePorId(id);
    }
}