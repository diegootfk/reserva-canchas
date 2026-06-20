package com.reservacanchas.cl.notificacion_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservacanchas.cl.notificacion_service.model.Notificacion;
import com.reservacanchas.cl.notificacion_service.service.NotificacionService;

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

@WebMvcTest(NotificacionController.class)
class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacionService notificacionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void debeCrearNotificacion() throws Exception {

        Notificacion notificacion = new Notificacion(
                1L,1L,1L,
                "Reserva confirmada",
                "EMAIL",
                "2025-06-20",
                "ENVIADA"
        );

        when(notificacionService.guardar(any()))
                .thenReturn(notificacion);

        mockMvc.perform(post("/notificaciones")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notificacion)))
                .andExpect(status().isCreated());
    }

    @Test
    void debeListarNotificaciones() throws Exception {

        Notificacion notificacion = new Notificacion(
                1L,1L,1L,
                "Reserva confirmada",
                "EMAIL",
                "2025-06-20",
                "ENVIADA"
        );

        when(notificacionService.listar())
                .thenReturn(List.of(notificacion));

        mockMvc.perform(get("/notificaciones")
                        .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void debeBuscarPorId() throws Exception {

        Notificacion notificacion = new Notificacion(
                1L,1L,1L,
                "Reserva confirmada",
                "EMAIL",
                "2025-06-20",
                "ENVIADA"
        );

        when(notificacionService.buscarPorId(1L))
                .thenReturn(notificacion);

        mockMvc.perform(get("/notificaciones/1")
                        .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void debeEliminarNotificacion() throws Exception {

        doNothing().when(notificacionService)
                .eliminar(1L);

        mockMvc.perform(delete("/notificaciones/1")
                        .with(jwt()))
                .andExpect(status().isNoContent());
    }

    @Test
    void debeVerificarExistencia() throws Exception {

        when(notificacionService.existePorId(1L))
                .thenReturn(true);

        mockMvc.perform(get("/notificaciones/1/exists"))
                .andExpect(status().isOk());
    }
}