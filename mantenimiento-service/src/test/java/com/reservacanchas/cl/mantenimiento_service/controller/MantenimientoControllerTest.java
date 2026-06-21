package com.reservacanchas.cl.mantenimiento_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservacanchas.cl.mantenimiento_service.dto.MantenimientoDTO;
import com.reservacanchas.cl.mantenimiento_service.model.Mantenimiento;
import com.reservacanchas.cl.mantenimiento_service.service.MantenimientoService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MantenimientoController.class)
class MantenimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MantenimientoService mantenimientoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void debeCrearMantenimiento() throws Exception {

        MantenimientoDTO dto = new MantenimientoDTO(
                1L,
                "2025-06-20",
                "2025-06-21",
                "Cambio de césped",
                "PENDIENTE"
        );

        Mantenimiento mantenimiento = new Mantenimiento(
                1L,
                1L,
                "2025-06-20",
                "2025-06-21",
                "Cambio de césped",
                "PENDIENTE"
        );

        when(mantenimientoService.guardar(any(MantenimientoDTO.class)))
                .thenReturn(mantenimiento);

        mockMvc.perform(post("/mantenimientos")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(mantenimientoService)
                .guardar(any(MantenimientoDTO.class));
    }

    @Test
    void debeListarMantenimientos() throws Exception {

        Mantenimiento mantenimiento = new Mantenimiento(
                1L,
                1L,
                "2025-06-20",
                "2025-06-21",
                "Cambio de césped",
                "PENDIENTE"
        );

        when(mantenimientoService.listar())
                .thenReturn(List.of(mantenimiento));

        mockMvc.perform(get("/mantenimientos")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(mantenimientoService).listar();
    }

    @Test
    void debeBuscarPorId() throws Exception {

        Mantenimiento mantenimiento = new Mantenimiento(
                1L,
                1L,
                "2025-06-20",
                "2025-06-21",
                "Cambio de césped",
                "PENDIENTE"
        );

        when(mantenimientoService.buscarPorId(1L))
                .thenReturn(mantenimiento);

        mockMvc.perform(get("/mantenimientos/1")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(mantenimientoService).buscarPorId(1L);
    }

    @Test
    void debeActualizarMantenimiento() throws Exception {

        MantenimientoDTO dto = new MantenimientoDTO(
                1L,
                "2025-06-25",
                "2025-06-26",
                "Mantención completa",
                "FINALIZADO"
        );

        Mantenimiento mantenimiento = new Mantenimiento(
                1L,
                1L,
                "2025-06-25",
                "2025-06-26",
                "Mantención completa",
                "FINALIZADO"
        );

        when(mantenimientoService.actualizar(
                eq(1L),
                any(MantenimientoDTO.class)))
                .thenReturn(mantenimiento);

        mockMvc.perform(put("/mantenimientos/1")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(mantenimientoService)
                .actualizar(eq(1L), any(MantenimientoDTO.class));
    }

    @Test
    void debeEliminarMantenimiento() throws Exception {

        doNothing().when(mantenimientoService)
                .eliminar(1L);

        mockMvc.perform(delete("/mantenimientos/1")
                        .with(jwt()))
                .andExpect(status().isNoContent());

        verify(mantenimientoService)
                .eliminar(1L);
    }

    @Test
    void debeVerificarExistencia() throws Exception {

        when(mantenimientoService.existePorId(1L))
                .thenReturn(true);

        mockMvc.perform(get("/mantenimientos/1/exists")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(mantenimientoService)
                .existePorId(1L);
    }
}