package com.reservacanchas.cl.pago_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reservacanchas.cl.pago_service.dto.PagoDTO;
import com.reservacanchas.cl.pago_service.model.Pago;
import com.reservacanchas.cl.pago_service.service.PagoService;

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

@WebMvcTest(PagoController.class)
class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagoService pagoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Debe crear pago")
    void debeCrearPago() throws Exception {

        PagoDTO dto = new PagoDTO(
                1L,
                25000.0,
                "TRANSFERENCIA"
        );

        Pago pago = new Pago(
                1L,
                1L,
                25000.0,
                "TRANSFERENCIA",
                "PAGADO"
        );

        when(pagoService.guardar(any(PagoDTO.class)))
                .thenReturn(pago);

        mockMvc.perform(post("/pagos")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());

        verify(pagoService).guardar(any(PagoDTO.class));
    }

    @Test
    @DisplayName("Debe listar pagos")
    void debeListarPagos() throws Exception {

        Pago pago = new Pago(
                1L,
                1L,
                25000.0,
                "TRANSFERENCIA",
                "PAGADO"
        );

        when(pagoService.listar())
                .thenReturn(List.of(pago));

        mockMvc.perform(get("/pagos")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(pagoService).listar();
    }

    @Test
    @DisplayName("Debe buscar pago por id")
    void debeBuscarPagoPorId() throws Exception {

        Pago pago = new Pago(
                1L,
                1L,
                25000.0,
                "TRANSFERENCIA",
                "PAGADO"
        );

        when(pagoService.buscarPorId(1L))
                .thenReturn(pago);

        mockMvc.perform(get("/pagos/1")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(pagoService).buscarPorId(1L);
    }

    @Test
    @DisplayName("Debe eliminar pago")
    void debeEliminarPago() throws Exception {

        doNothing().when(pagoService)
                .eliminar(1L);

        mockMvc.perform(delete("/pagos/1")
                        .with(jwt()))
                .andExpect(status().isNoContent());

        verify(pagoService).eliminar(1L);
    }

    @Test
    @DisplayName("Debe verificar existencia")
    void debeVerificarExistencia() throws Exception {

        when(pagoService.existePorId(1L))
                .thenReturn(true);

        mockMvc.perform(get("/pagos/1/exists")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(pagoService).existePorId(1L);
    }

    @Test
    @DisplayName("Debe buscar pagos por metodo")
    void debeBuscarPorMetodo() throws Exception {

        Pago pago = new Pago(
                1L,
                1L,
                25000.0,
                "TRANSFERENCIA",
                "PAGADO"
        );

        when(pagoService.buscarPorMetodoPago("TRANSFERENCIA"))
                .thenReturn(List.of(pago));

        mockMvc.perform(get("/pagos/metodo/TRANSFERENCIA")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(pagoService).buscarPorMetodoPago("TRANSFERENCIA");
    }

    @Test
    @DisplayName("Debe buscar pagos por estado")
    void debeBuscarPorEstado() throws Exception {

        Pago pago = new Pago(
                1L,
                1L,
                25000.0,
                "TRANSFERENCIA",
                "PAGADO"
        );

        when(pagoService.buscarPorEstadoPago("PAGADO"))
                .thenReturn(List.of(pago));

        mockMvc.perform(get("/pagos/estado/PAGADO")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(pagoService).buscarPorEstadoPago("PAGADO");
    }

    @Test
    @DisplayName("Debe buscar pagos por reserva")
    void debeBuscarPorReserva() throws Exception {

        Pago pago = new Pago(
                1L,
                1L,
                25000.0,
                "TRANSFERENCIA",
                "PAGADO"
        );

        when(pagoService.buscarPorReserva(1L))
                .thenReturn(List.of(pago));

        mockMvc.perform(get("/pagos/reserva/1")
                        .with(jwt()))
                .andExpect(status().isOk());

        verify(pagoService).buscarPorReserva(1L);
    }
}