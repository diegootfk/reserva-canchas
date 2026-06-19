package com.reservacanchas.cl.notificacion_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "id_reserva")
    private Long idReserva;

    private String mensaje;

    @Column(name = "tipo_notificacion")
    private String tipoNotificacion;

    @Column(name = "fecha_envio")
    private String fechaEnvio;

    private String estado;
}