package com.reservacanchas.cl.mantenimiento_service.assembler;

import com.reservacanchas.cl.mantenimiento_service.model.Mantenimiento;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class MantenimientoAssembler {

    private static final String API_GATEWAY = "http://localhost:7090";

    public EntityModel<Mantenimiento> toModel(Mantenimiento mantenimiento) {

        return EntityModel.of(
                mantenimiento,
                Link.of(API_GATEWAY + "/mantenimientos/" + mantenimiento.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/mantenimientos").withRel("mantenimientos"),
                Link.of(API_GATEWAY + "/mantenimientos/" + mantenimiento.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/canchas/" + mantenimiento.getIdCancha()).withRel("cancha")
        );
    }
}