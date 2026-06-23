package com.reservacanchas.cl.pago_service.assembler;

import com.reservacanchas.cl.pago_service.model.Pago;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
public class PagoAssembler {

    private static final String API_GATEWAY = "http://localhost:7090";

    public EntityModel<Pago> toModel(Pago pago) {

        return EntityModel.of(
                pago,
                Link.of(API_GATEWAY + "/pagos/" + pago.getId()).withSelfRel(),
                Link.of(API_GATEWAY + "/pagos").withRel("pagos"),
                Link.of(API_GATEWAY + "/pagos/" + pago.getId() + "/exists").withRel("existe"),
                Link.of(API_GATEWAY + "/pagos/metodo/" + pago.getMetodoPago()).withRel("pagos-por-metodo"),
                Link.of(API_GATEWAY + "/pagos/estado/" + pago.getEstadoPago()).withRel("pagos-por-estado"),
                Link.of(API_GATEWAY + "/pagos/reserva/" + pago.getIdReserva()).withRel("pagos-por-reserva"),
                Link.of(API_GATEWAY + "/reservas/" + pago.getIdReserva()).withRel("reserva")
        );
    }
}