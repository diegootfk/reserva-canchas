package com.reservacanchas.cl.resena_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservacanchas.cl.resena_service.model.Resena;
import com.reservacanchas.cl.resena_service.service.ResenaService;

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

@WebMvcTest(ResenaController.class)
class ResenaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResenaService resenaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void debeCrearResena() throws Exception {

        Resena resena = new Resena(
                1L,1L,1L,1L,
                5,
                "Excelente cancha",
                "2025-06-20"
        );

        when(resenaService.guardar(any()))
                .thenReturn(resena);

        mockMvc.perform(post("/resenas")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(resena)))
                .andExpect(status().isCreated());
    }

    @Test
    void debeListarResenas() throws Exception {

        Resena resena = new Resena(
                1L,1L,1L,1L,
                5,
                "Excelente cancha",
                "2025-06-20"
        );

        when(resenaService.listar())
                .thenReturn(List.of(resena));

        mockMvc.perform(get("/resenas")
                        .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void debeBuscarPorId() throws Exception {

        Resena resena = new Resena(
                1L,1L,1L,1L,
                5,
                "Excelente cancha",
                "2025-06-20"
        );

        when(resenaService.buscarPorId(1L))
                .thenReturn(resena);

        mockMvc.perform(get("/resenas/1")
                        .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void debeEliminarResena() throws Exception {

        doNothing().when(resenaService)
                .eliminar(1L);

        mockMvc.perform(delete("/resenas/1")
                        .with(jwt()))
                .andExpect(status().isNoContent());
    }

    @Test
    void debeVerificarExistencia() throws Exception {

        when(resenaService.existePorId(1L))
                .thenReturn(true);

        mockMvc.perform(get("/resenas/1/exists"))
                .andExpect(status().isOk());
    }
}