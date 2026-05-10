package com.reservacanchas.cl.cancha_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanchaDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El tipo de cancha es obligatorio")
    private String tipoCancha;

    @NotNull(message = "El precio por hora es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precioHora;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser mayor a 0")
    private Integer capacidad;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}