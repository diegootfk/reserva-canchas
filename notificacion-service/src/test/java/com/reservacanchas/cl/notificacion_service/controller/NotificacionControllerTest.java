package com.reservacanchas.cl.notificacion_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservacanchas.cl.notificacion_service.dto.NotificacionDTO;
import com.reservacanchas.cl.notificacion_service.model.Notificacion;
import com.reservacanchas.cl.notificacion_service.service.NotificacionService;

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

        NotificacionDTO dto = new NotificacionDTO(
                1L,
                1L,
                "Reserva confirmada",
                "EMAIL",
                "2025-06-20",
                "ENVIADA"
        );

        Notificacion notificacion = new Notificacion(
                1L,
                1L,
                1L,
                "Reserva confirmada",
                "EMAIL",
                "2025-06-20",
                "ENVIADA"
        );

        when(notificacionService.guardar(any(NotificacionDTO.class)))
                .thenReturn(notificacion);

        mockMvc.perform(post("/notificaciones")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(notificacionService)
                .guardar(any(NotificacionDTO.class));
    }

    @Test
    void debeListarNotificaciones() throws Exception {

        Notificacion notificacion = new Notificacion(
                1L,
                1L,
                1L,
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

        verify(notificacionService).listar();
    }

    @Test
    void debeBuscarPorId() throws Exception {

        Notificacion notificacion = new Notificacion(
                1L,
                1L,
                1L,
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

        verify(notificacionService)
                .buscarPorId(1L);
    }

    @Test
    void debeActualizarNotificacion() throws Exception {

        NotificacionDTO dto = new NotificacionDTO(
                1L,
                1L,
                "Reserva modificada",
                "SMS",
                "2025-06-21",
                "PENDIENTE"
        );

        Notificacion notificacion = new Notificacion(
                1L,
                1L,
                1L,
                "Reserva modificada",
                "SMS",
                "2025-06-21",
                "PENDIENTE"
        );

        when(notificacionService.actualizar(eq(1L), any(NotificacionDTO.class)))
                .thenReturn(notificacion);

        mockMvc.perform(put("/notificaciones/1")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(notificacionService)
                .actualizar(eq(1L), any(NotificacionDTO.class));
    }

    @Test
    void debeEliminarNotificacion() throws Exception {

        doNothing().when(notificacionService)
                .eliminar(1L);

        mockMvc.perform(delete("/notificaciones/1")
                        .with(jwt()))
                .andExpect(status().isNoContent());

        verify(notificacionService)
                .eliminar(1L);
    }

    @Test
    void debeVerificarExistencia() throws Exception {

        when(notificacionService.existePorId(1L))
                .thenReturn(true);

        mockMvc.perform(get("/notificaciones/1/exists")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(notificacionService)
                .existePorId(1L);
    }
}