package com.reservacanchas.cl.disponibilidad_service.assembler;

import com.reservacanchas.cl.disponibilidad_service.model.Disponibilidad;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class DisponibilidadAssembler {

    private static final String API_GATEWAY = "http://localhost:7090";

    public EntityModel<Disponibilidad> toModel(
            Disponibilidad disponibilidad) {

        return EntityModel.of(
                disponibilidad,
                Link.of(API_GATEWAY + "/disponibilidades/" + disponibilidad.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/disponibilidades").withRel("disponibilidades"),
                Link.of(API_GATEWAY + "/disponibilidades/" + disponibilidad.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/canchas/" + disponibilidad.getIdCancha()).withRel("cancha")
        );
    }
}