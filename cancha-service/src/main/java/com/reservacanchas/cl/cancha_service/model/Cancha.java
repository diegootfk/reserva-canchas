package com.reservacanchas.cl.cancha_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "canchas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cancha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String tipoCancha;
    private Double precioHora;
    private Integer capacidad;
    private String estado;
}