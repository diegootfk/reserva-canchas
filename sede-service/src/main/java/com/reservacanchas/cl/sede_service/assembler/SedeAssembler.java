package com.reservacanchas.cl.sede_service.assembler;

import com.reservacanchas.cl.sede_service.model.Sede;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class SedeAssembler {

    private static final String API_GATEWAY = "http://localhost:7090";

    public EntityModel<Sede> toModel(Sede sede) {

        return EntityModel.of(
                sede,
                Link.of(API_GATEWAY + "/sedes/" + sede.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/sedes").withRel("sedes"),
                Link.of(API_GATEWAY + "/sedes/" + sede.getId() + "/exists").withRel("existe")
        );
    }
}