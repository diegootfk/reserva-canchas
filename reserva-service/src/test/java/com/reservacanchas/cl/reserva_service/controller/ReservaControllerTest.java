package com.reservacanchas.cl.reserva_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservacanchas.cl.reserva_service.dto.ReservaDTO;
import com.reservacanchas.cl.reserva_service.model.Reserva;
import com.reservacanchas.cl.reserva_service.service.ReservaService;

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

@WebMvcTest(ReservaController.class)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe crear reserva")
    void debeCrearReserva() throws Exception {

        ReservaDTO dto = new ReservaDTO(
                1L,
                1L,
                25000.0
        );

        Reserva reserva = new Reserva(
                1L,
                1L,
                1L,
                25000.0,
                "CONFIRMADA"
        );

        when(reservaService.guardar(any(ReservaDTO.class)))
                .thenReturn(reserva);

        mockMvc.perform(post("/reservas")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(reservaService).guardar(any(ReservaDTO.class));
    }

    @Test
    @DisplayName("Debe listar reservas")
    void debeListarReservas() throws Exception {

        Reserva reserva = new Reserva(
                1L, 1L, 1L, 25000.0, "CONFIRMADA"
        );

        when(reservaService.listar())
                .thenReturn(List.of(reserva));

        mockMvc.perform(get("/reservas")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(reservaService).listar();
    }

    @Test
    @DisplayName("Debe buscar reserva por id")
    void debeBuscarReservaPorId() throws Exception {

        Reserva reserva = new Reserva(
                1L, 1L, 1L, 25000.0, "CONFIRMADA"
        );

        when(reservaService.buscarPorId(1L))
                .thenReturn(reserva);

        mockMvc.perform(get("/reservas/1")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(reservaService).buscarPorId(1L);
    }

    @Test
    @DisplayName("Debe actualizar reserva")
    void debeActualizarReserva() throws Exception {

        ReservaDTO dto = new ReservaDTO(
                1L,
                1L,
                30000.0
        );

        Reserva reserva = new Reserva(
                1L,
                1L,
                1L,
                30000.0,
                "CONFIRMADA"
        );

        when(reservaService.actualizar(eq(1L), any(ReservaDTO.class)))
                .thenReturn(reserva);

        mockMvc.perform(put("/reservas/1")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(reservaService)
                .actualizar(eq(1L), any(ReservaDTO.class));
    }

    @Test
    @DisplayName("Debe eliminar reserva")
    void debeEliminarReserva() throws Exception {

        doNothing().when(reservaService)
                .eliminar(1L);

        mockMvc.perform(delete("/reservas/1")
                        .with(jwt()))
                .andExpect(status().isNoContent());

        verify(reservaService).eliminar(1L);
    }

    @Test
    @DisplayName("Debe verificar existencia")
    void debeVerificarExistencia() throws Exception {

        when(reservaService.existePorId(1L))
                .thenReturn(true);

        mockMvc.perform(get("/reservas/1/exists"))
                .andExpect(status().isOk());

        verify(reservaService).existePorId(1L);
    }

    @Test
    @DisplayName("Debe buscar reservas por estado")
    void debeBuscarPorEstado() throws Exception {

        Reserva reserva = new Reserva(
                1L, 1L, 1L, 25000.0, "CONFIRMADA"
        );

        when(reservaService.buscarPorEstado("CONFIRMADA"))
                .thenReturn(List.of(reserva));

        mockMvc.perform(get("/reservas/estado/CONFIRMADA")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(reservaService)
                .buscarPorEstado("CONFIRMADA");
    }

    @Test
    @DisplayName("Debe buscar reservas por usuario")
    void debeBuscarPorUsuario() throws Exception {

        Reserva reserva = new Reserva(
                1L, 1L, 1L, 25000.0, "CONFIRMADA"
        );

        when(reservaService.buscarPorUsuario(1L))
                .thenReturn(List.of(reserva));

        mockMvc.perform(get("/reservas/usuario/1")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(reservaService)
                .buscarPorUsuario(1L);
    }

    @Test
    @DisplayName("Debe buscar reservas por cancha")
    void debeBuscarPorCancha() throws Exception {

        Reserva reserva = new Reserva(
                1L, 1L, 1L, 25000.0, "CONFIRMADA"
        );

        when(reservaService.buscarPorCancha(1L))
                .thenReturn(List.of(reserva));

        mockMvc.perform(get("/reservas/cancha/1")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(reservaService)
                .buscarPorCancha(1L);
    }
}