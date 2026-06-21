package com.reservacanchas.cl.resena_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResenaDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El ID de la cancha es obligatorio")
    private Long idCancha;

    @NotNull(message = "El ID de la reserva es obligatorio")
    private Long idReserva;

    @NotNull(message = "La calificación es obligatoria")
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    private Integer calificacion;

    @NotBlank(message = "El comentario es obligatorio")
    private String comentario;

    @NotBlank(message = "La fecha de la reseña es obligatoria")
    private String fechaResena;
}