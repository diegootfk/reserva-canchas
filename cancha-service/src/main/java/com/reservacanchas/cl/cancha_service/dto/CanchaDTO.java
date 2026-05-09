package com.reservacanchas.cl.cancha_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanchaDTO {

    private String nombre;
    private String tipoCancha;
    private Double precioHora;
    private Integer capacidad;
    private String estado;
}