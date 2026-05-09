package com.reservacanchas.cl.reserva_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {

    private Long idUsuario;
    private Long idCancha;
    private Double total;
}