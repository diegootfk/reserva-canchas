package com.reservacanchas.cl.horario_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservacanchas.cl.horario_service.model.Horario;
import com.reservacanchas.cl.horario_service.service.HorarioService;

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

@WebMvcTest(HorarioController.class)
class HorarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HorarioService horarioService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe crear horario")
    void debeCrearHorario() throws Exception {

        Horario horario = new Horario(
                1L,
                1L,
                "LUNES",
                "09:00",
                "10:00",
                "ACTIVO"
        );

        when(horarioService.guardar(any(Horario.class)))
                .thenReturn(horario);

        mockMvc.perform(post("/horarios")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(horario)))
                .andExpect(status().isCreated());

        verify(horarioService).guardar(any(Horario.class));
    }

    @Test
    @DisplayName("Debe listar horarios")
    void debeListarHorarios() throws Exception {

        Horario horario = new Horario(
                1L,
                1L,
                "LUNES",
                "09:00",
                "10:00",
                "ACTIVO"
        );

        when(horarioService.listar())
                .thenReturn(List.of(horario));

        mockMvc.perform(get("/horarios")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(horarioService).listar();
    }

    @Test
    @DisplayName("Debe buscar horario por id")
    void debeBuscarHorarioPorId() throws Exception {

        Horario horario = new Horario(
                1L,
                1L,
                "LUNES",
                "09:00",
                "10:00",
                "ACTIVO"
        );

        when(horarioService.buscarPorId(1L))
                .thenReturn(horario);

        mockMvc.perform(get("/horarios/1")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(horarioService).buscarPorId(1L);
    }

    @Test
    @DisplayName("Debe actualizar horario")
    void debeActualizarHorario() throws Exception {

        Horario horario = new Horario(
                1L,
                1L,
                "LUNES",
                "09:00",
                "10:00",
                "ACTIVO"
        );

        when(horarioService.actualizar(eq(1L), any(Horario.class)))
                .thenReturn(horario);

        mockMvc.perform(put("/horarios/1")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(horario)))
                .andExpect(status().isOk());

        verify(horarioService).actualizar(eq(1L), any(Horario.class));
    }

    @Test
    @DisplayName("Debe eliminar horario")
    void debeEliminarHorario() throws Exception {

        doNothing().when(horarioService)
                .eliminar(1L);

        mockMvc.perform(delete("/horarios/1")
                        .with(jwt()))
                .andExpect(status().isNoContent());

        verify(horarioService).eliminar(1L);
    }

    @Test
    @DisplayName("Debe verificar existencia")
    void debeVerificarExistencia() throws Exception {

        when(horarioService.existePorId(1L))
                .thenReturn(true);

        mockMvc.perform(get("/horarios/1/exists")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(horarioService).existePorId(1L);
    }
}