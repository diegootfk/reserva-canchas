package com.reservacanchas.cl.cancha_service.assembler;

import com.reservacanchas.cl.cancha_service.model.Cancha;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class CanchaAssembler {

    private static final String API_GATEWAY = "http://localhost:7090";

    public EntityModel<Cancha> toModel(Cancha cancha) {

        return EntityModel.of(
                cancha,
                Link.of(API_GATEWAY + "/canchas/" + cancha.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/canchas").withRel("canchas"),
                Link.of(API_GATEWAY + "/canchas/" + cancha.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/reservas/cancha/" + cancha.getId()).withRel("reservas"),
                Link.of(API_GATEWAY + "/disponibilidades/cancha/" + cancha.getId()).withRel("disponibilidades"),
                Link.of(API_GATEWAY + "/horarios/cancha/" + cancha.getId()).withRel("horarios"),
                Link.of(API_GATEWAY + "/resenas/cancha/" + cancha.getId()).withRel("resenas"),
                Link.of(API_GATEWAY + "/mantenimientos/cancha/" + cancha.getId()).withRel("mantenimientos")
        );
    }
}