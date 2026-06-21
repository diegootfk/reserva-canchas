package com.reservacanchas.cl.horario_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HorarioDTO {

    @NotNull(message = "El ID de la cancha es obligatorio")
    private Long idCancha;

    @NotBlank(message = "El día de la semana es obligatorio")
    private String diaSemana;

    @NotBlank(message = "La hora de inicio es obligatoria")
    private String horaInicio;

    @NotBlank(message = "La hora de fin es obligatoria")
    private String horaFin;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}