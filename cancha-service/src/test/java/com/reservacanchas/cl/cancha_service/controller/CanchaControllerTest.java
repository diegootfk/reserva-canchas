package com.reservacanchas.cl.cancha_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservacanchas.cl.cancha_service.dto.CanchaDTO;
import com.reservacanchas.cl.cancha_service.model.Cancha;
import com.reservacanchas.cl.cancha_service.service.CanchaService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CanchaController.class)
class CanchaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CanchaService canchaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe crear cancha correctamente")
    void debeCrearCancha() throws Exception {

        CanchaDTO dto = new CanchaDTO(
                "Cancha 1",
                "Futbolito",
                25000.0,
                14,
                "ACTIVA"
        );

        Cancha cancha = new Cancha(
                1L,
                "Cancha 1",
                "Futbolito",
                25000.0,
                14,
                "ACTIVA"
        );

        when(canchaService.guardar(any(CanchaDTO.class)))
                .thenReturn(cancha);

        mockMvc.perform(post("/canchas")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(canchaService, times(1))
                .guardar(any(CanchaDTO.class));
    }

    @Test
    @DisplayName("Debe listar canchas")
    void debeListarCanchas() throws Exception {

        Cancha cancha = new Cancha(
                1L,
                "Cancha 1",
                "Futbolito",
                25000.0,
                14,
                "ACTIVA"
        );

        when(canchaService.listar())
                .thenReturn(List.of(cancha));

        mockMvc.perform(get("/canchas")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(canchaService, times(1))
                .listar();
    }

    @Test
    @DisplayName("Debe buscar cancha por ID")
    void debeBuscarCanchaPorId() throws Exception {

        Cancha cancha = new Cancha(
                1L,
                "Cancha 1",
                "Futbolito",
                25000.0,
                14,
                "ACTIVA"
        );

        when(canchaService.buscarPorId(1L))
                .thenReturn(cancha);

        mockMvc.perform(get("/canchas/1")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(canchaService, times(1))
                .buscarPorId(1L);
    }

    @Test
    @DisplayName("Debe eliminar cancha")
    void debeEliminarCancha() throws Exception {

        doNothing().when(canchaService)
                .eliminar(1L);

        mockMvc.perform(delete("/canchas/1")
                        .with(jwt()))
                .andExpect(status().isNoContent());

        verify(canchaService, times(1))
                .eliminar(1L);
    }

    @Test
    @DisplayName("Debe verificar existencia de cancha")
    void debeVerificarExistencia() throws Exception {

        when(canchaService.existePorId(1L))
                .thenReturn(true);

        mockMvc.perform(get("/canchas/1/exists"))
                .andExpect(status().isOk());

        verify(canchaService, times(1))
                .existePorId(1L);
    }
}