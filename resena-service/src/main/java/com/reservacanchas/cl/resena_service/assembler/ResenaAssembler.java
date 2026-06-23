package com.reservacanchas.cl.resena_service.assembler;

import com.reservacanchas.cl.resena_service.model.Resena;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class ResenaAssembler {

    private static final String API_GATEWAY = "http://localhost:7090";

    public EntityModel<Resena> toModel(Resena resena) {

        return EntityModel.of(
                resena,
                Link.of(API_GATEWAY + "/resenas/" + resena.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/resenas").withRel("resenas"),
                Link.of(API_GATEWAY + "/resenas/" + resena.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/usuarios/" + resena.getIdUsuario()).withRel("usuario"),
                Link.of(API_GATEWAY + "/canchas/" + resena.getIdCancha()).withRel("cancha"),
                Link.of(API_GATEWAY + "/reservas/" + resena.getIdReserva()).withRel("reserva")
        );
    }
}