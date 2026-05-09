package com.reservacanchas.cl.usuario_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String telefono;
    private String estado;
}