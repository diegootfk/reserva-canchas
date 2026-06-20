package com.reservacanchas.cl.disponibilidad_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservacanchas.cl.disponibilidad_service.model.Disponibilidad;
import com.reservacanchas.cl.disponibilidad_service.service.DisponibilidadService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DisponibilidadController.class)
class DisponibilidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DisponibilidadService disponibilidadService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe crear disponibilidad")
    void debeCrearDisponibilidad() throws Exception {

        Disponibilidad disponibilidad = new Disponibilidad(
                1L,1L,"2025-06-20","09:00","10:00","DISPONIBLE"
        );

        when(disponibilidadService.guardar(any()))
                .thenReturn(disponibilidad);

        mockMvc.perform(post("/disponibilidades")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(disponibilidad)))
                .andExpect(status().isCreated());
    }

    @Test
    void debeListarDisponibilidades() throws Exception {

        Disponibilidad disponibilidad = new Disponibilidad(
                1L,1L,"2025-06-20","09:00","10:00","DISPONIBLE"
        );

        when(disponibilidadService.listar())
                .thenReturn(List.of(disponibilidad));

        mockMvc.perform(get("/disponibilidades")
                        .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void debeBuscarPorId() throws Exception {

        Disponibilidad disponibilidad = new Disponibilidad(
                1L,1L,"2025-06-20","09:00","10:00","DISPONIBLE"
        );

        when(disponibilidadService.buscarPorId(1L))
                .thenReturn(disponibilidad);

        mockMvc.perform(get("/disponibilidades/1")
                        .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void debeEliminarDisponibilidad() throws Exception {

        doNothing().when(disponibilidadService)
                .eliminar(1L);

        mockMvc.perform(delete("/disponibilidades/1")
                        .with(jwt()))
                .andExpect(status().isNoContent());
    }

    @Test
    void debeVerificarExistencia() throws Exception {

        when(disponibilidadService.existePorId(1L))
                .thenReturn(true);

        mockMvc.perform(get("/disponibilidades/1/exists"))
                .andExpect(status().isOk());
    }
}