package com.reservacanchas.cl.pago_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pagos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_reserva")
    private Long idReserva;

    private Double monto;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @Column(name = "estado_pago")
    private String estadoPago;
}