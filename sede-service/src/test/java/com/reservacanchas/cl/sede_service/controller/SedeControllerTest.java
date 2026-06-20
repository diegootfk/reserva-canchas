package com.reservacanchas.cl.sede_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservacanchas.cl.sede_service.model.Sede;
import com.reservacanchas.cl.sede_service.service.SedeService;

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

@WebMvcTest(SedeController.class)
class SedeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SedeService sedeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe crear sede")
    void debeCrearSede() throws Exception {

        Sede sede = new Sede(
                1L,
                "Antonio Varas",
                "Av. Antonio Varas 666",
                "Providencia",
                "22223333",
                "ACTIVA"
        );

        when(sedeService.guardar(any(Sede.class)))
                .thenReturn(sede);

        mockMvc.perform(post("/sedes")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sede)))
                .andExpect(status().isCreated());

        verify(sedeService).guardar(any(Sede.class));
    }

    @Test
    @DisplayName("Debe listar sedes")
    void debeListarSedes() throws Exception {

        Sede sede = new Sede(
                1L,
                "Antonio Varas",
                "Av. Antonio Varas 666",
                "Providencia",
                "22223333",
                "ACTIVA"
        );

        when(sedeService.listar())
                .thenReturn(List.of(sede));

        mockMvc.perform(get("/sedes")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(sedeService).listar();
    }

    @Test
    @DisplayName("Debe buscar sede por id")
    void debeBuscarSedePorId() throws Exception {

        Sede sede = new Sede(
                1L,
                "Antonio Varas",
                "Av. Antonio Varas 666",
                "Providencia",
                "22223333",
                "ACTIVA"
        );

        when(sedeService.buscarPorId(1L))
                .thenReturn(sede);

        mockMvc.perform(get("/sedes/1")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(sedeService).buscarPorId(1L);
    }

    @Test
    @DisplayName("Debe actualizar sede")
    void debeActualizarSede() throws Exception {

        Sede sede = new Sede(
                1L,
                "Antonio Varas",
                "Av. Antonio Varas 666",
                "Providencia",
                "22223333",
                "ACTIVA"
        );

        when(sedeService.actualizar(eq(1L), any(Sede.class)))
                .thenReturn(sede);

        mockMvc.perform(put("/sedes/1")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sede)))
                .andExpect(status().isOk());

        verify(sedeService).actualizar(eq(1L), any(Sede.class));
    }

    @Test
    @DisplayName("Debe eliminar sede")
    void debeEliminarSede() throws Exception {

        doNothing().when(sedeService)
                .eliminar(1L);

        mockMvc.perform(delete("/sedes/1")
                        .with(jwt()))
                .andExpect(status().isNoContent());

        verify(sedeService).eliminar(1L);
    }

    @Test
    @DisplayName("Debe verificar existencia")
    void debeVerificarExistencia() throws Exception {

        when(sedeService.existePorId(1L))
                .thenReturn(true);

        mockMvc.perform(get("/sedes/1/exists")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(sedeService).existePorId(1L);
    }
}