package com.reservacanchas.cl.reserva_service.assembler;

import com.reservacanchas.cl.reserva_service.model.Reserva;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class ReservaAssembler {

    private static final String API_GATEWAY = "http://localhost:7090";

    public EntityModel<Reserva> toModel(Reserva reserva) {

        return EntityModel.of(
                reserva,
                Link.of(API_GATEWAY + "/reservas/" + reserva.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/reservas").withRel("reservas"),
                Link.of(API_GATEWAY + "/reservas/" + reserva.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/reservas/estado/" + reserva.getEstado()).withRel("reservas-por-estado"),
                Link.of(API_GATEWAY + "/reservas/usuario/" + reserva.getIdUsuario()).withRel("reservas-por-usuario"),
                Link.of(API_GATEWAY + "/reservas/cancha/" + reserva.getIdCancha()).withRel("reservas-por-cancha"),
                Link.of(API_GATEWAY + "/usuarios/" + reserva.getIdUsuario()).withRel("usuario"),
                Link.of(API_GATEWAY + "/canchas/" + reserva.getIdCancha()).withRel("cancha"),
                Link.of(API_GATEWAY + "/pagos/reserva/" + reserva.getId()).withRel("pagos")
        );
    }
}