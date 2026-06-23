package com.reservacanchas.cl.horario_service.assembler;

import com.reservacanchas.cl.horario_service.model.Horario;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class HorarioAssembler {

    private static final String API_GATEWAY = "http://localhost:7090";

    public EntityModel<Horario> toModel(Horario horario) {

        return EntityModel.of(
                horario,
                Link.of(API_GATEWAY + "/horarios/" + horario.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/horarios").withRel("horarios"),
                Link.of(API_GATEWAY + "/horarios/" + horario.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/canchas/" + horario.getIdCancha()).withRel("cancha")
        );
    }
}