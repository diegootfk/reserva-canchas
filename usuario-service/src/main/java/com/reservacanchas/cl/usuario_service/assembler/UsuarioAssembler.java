package com.reservacanchas.cl.usuario_service.assembler;

import com.reservacanchas.cl.usuario_service.model.Usuario;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAssembler {

    private static final String API_GATEWAY = "http://localhost:7090";

    public EntityModel<Usuario> toModel(Usuario usuario) {

        return EntityModel.of(
                usuario,
                Link.of(API_GATEWAY + "/usuarios/" + usuario.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/usuarios").withRel("usuarios"),
                Link.of(API_GATEWAY + "/usuarios/" + usuario.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/reservas/usuario/" + usuario.getId()).withRel("reservas")
        );
    }
}