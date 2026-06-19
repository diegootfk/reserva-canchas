package com.reservacanchas.cl.reserva_service.service;

import com.reservacanchas.cl.reserva_service.dto.ReservaDTO;
import com.reservacanchas.cl.reserva_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.reserva_service.model.Reserva;
import com.reservacanchas.cl.reserva_service.repository.ReservaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    private Reserva reserva;
    private ReservaDTO reservaDTO;

    @BeforeEach
    void setUp() {

        reserva = new Reserva(
                1L,
                1L,
                1L,
                25000.0,
                "CONFIRMADA"
        );

        reservaDTO = new ReservaDTO(
                1L,
                1L,
                25000.0
        );
    }

    @Test
    void listarDebeRetornarReservas() {

        when(reservaRepository.findAll())
                .thenReturn(List.of(reserva));

        List<Reserva> resultado = reservaService.listar();

        assertEquals(1, resultado.size());

        verify(reservaRepository).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarReserva() {

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reserva));

        Reserva resultado = reservaService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscarPorIdDebeLanzarExcepcion() {

        when(reservaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservaService.buscarPorId(99L)
        );
    }

    @Test
    void actualizarDebeActualizarReserva() {

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reserva));

        when(reservaRepository.save(any(Reserva.class)))
                .thenReturn(reserva);

        Reserva resultado =
                reservaService.actualizar(1L, reservaDTO);

        assertNotNull(resultado);

        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void actualizarDebeLanzarExcepcionSiNoExiste() {

        when(reservaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> reservaService.actualizar(99L, reservaDTO)
        );
    }

    @Test
    void eliminarDebeEliminarReserva() {

        when(reservaRepository.findById(1L))
                .thenReturn(Optional.of(reserva));

        reservaService.eliminar(1L);

        verify(reservaRepository).delete(reserva);
    }

    @Test
    void existePorIdDebeRetornarTrue() {

        when(reservaRepository.existsById(1L))
                .thenReturn(true);

        assertTrue(reservaService.existePorId(1L));
    }

    @Test
    void existePorIdDebeRetornarFalse() {

        when(reservaRepository.existsById(99L))
                .thenReturn(false);

        assertFalse(reservaService.existePorId(99L));
    }

    @Test
    void buscarPorEstadoDebeRetornarLista() {

        when(reservaRepository.findByEstado("CONFIRMADA"))
                .thenReturn(List.of(reserva));

        assertEquals(
                1,
                reservaService.buscarPorEstado("CONFIRMADA").size()
        );
    }

    @Test
    void buscarPorUsuarioDebeRetornarLista() {

        when(reservaRepository.findByIdUsuario(1L))
                .thenReturn(List.of(reserva));

        assertEquals(
                1,
                reservaService.buscarPorUsuario(1L).size()
        );
    }

    @Test
    void buscarPorCanchaDebeRetornarLista() {

        when(reservaRepository.findByIdCancha(1L))
                .thenReturn(List.of(reserva));

        assertEquals(
                1,
                reservaService.buscarPorCancha(1L).size()
        );
    }
}