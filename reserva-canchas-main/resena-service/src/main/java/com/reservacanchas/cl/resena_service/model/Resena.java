package com.reservacanchas.cl.resena_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resenas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "id_cancha")
    private Long idCancha;

    @Column(name = "id_reserva")
    private Long idReserva;

    private Integer calificacion;
    private String comentario;

    @Column(name = "fecha_resena")
    private String fechaResena;
}