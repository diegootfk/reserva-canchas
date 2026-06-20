package com.reservacanchas.cl.mantenimiento_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MantenimientoDTO {

    @NotNull(message = "El ID de la cancha es obligatorio")
    private Long idCancha;

    @NotBlank(message = "La fecha de inicio es obligatoria")
    private String fechaInicio;

    @NotBlank(message = "La fecha de fin es obligatoria")
    private String fechaFin;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}