package com.reservacanchas.cl.notificacion_service.assembler;

import com.reservacanchas.cl.notificacion_service.model.Notificacion;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class NotificacionAssembler {

    private static final String API_GATEWAY = "http://localhost:7090";

    public EntityModel<Notificacion> toModel(Notificacion notificacion) {

        return EntityModel.of(
                notificacion,
                Link.of(API_GATEWAY + "/notificaciones/" + notificacion.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/notificaciones").withRel("notificaciones"),
                Link.of(API_GATEWAY + "/notificaciones/" + notificacion.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/usuarios/" + notificacion.getIdUsuario()).withRel("usuario"),
                Link.of(API_GATEWAY + "/reservas/" + notificacion.getIdReserva()).withRel("reserva")
        );
    }
}