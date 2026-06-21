package com.reservacanchas.cl.notificacion_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacionDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long idUsuario;

    @NotNull(message = "El ID de la reserva es obligatorio")
    private Long idReserva;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @NotBlank(message = "El tipo de notificación es obligatorio")
    private String tipoNotificacion;

    @NotBlank(message = "La fecha de envío es obligatoria")
    private String fechaEnvio;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}