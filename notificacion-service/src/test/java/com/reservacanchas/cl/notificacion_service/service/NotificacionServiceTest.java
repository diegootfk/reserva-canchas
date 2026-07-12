package com.reservacanchas.cl.notificacion_service.service;

import com.reservacanchas.cl.notificacion_service.dto.NotificacionDTO;
import com.reservacanchas.cl.notificacion_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.notificacion_service.model.Notificacion;
import com.reservacanchas.cl.notificacion_service.repository.NotificacionRepository;

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
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    private Notificacion notificacion;
    private NotificacionDTO notificacionDTO;

    @BeforeEach
    void setUp() {

        notificacion = new Notificacion(
                1L,
                1L,
                1L,
                "Reserva confirmada",
                "EMAIL",
                "2025-06-20",
                "ENVIADA"
        );

        notificacionDTO = new NotificacionDTO(
                1L,
                1L,
                "Reserva modificada",
                "SMS",
                "2025-06-21",
                "PENDIENTE"
        );
    }

    @Test
    void listarDebeRetornarNotificaciones() {

        when(notificacionRepository.findAll())
                .thenReturn(List.of(notificacion));

        List<Notificacion> resultado =
                notificacionService.listar();

        assertEquals(1, resultado.size());

        verify(notificacionRepository).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarNotificacion() {

        when(notificacionRepository.findById(1L))
                .thenReturn(Optional.of(notificacion));

        Notificacion resultado =
                notificacionService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(notificacionRepository).findById(1L);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcion() {

        when(notificacionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> notificacionService.buscarPorId(99L)
        );
    }

    @Test
    void actualizarDebeActualizarNotificacion() {

        Notificacion actualizada = new Notificacion(
                1L,
                1L,
                1L,
                "Reserva modificada",
                "SMS",
                "2025-06-21",
                "PENDIENTE"
        );

        when(notificacionRepository.findById(1L))
                .thenReturn(Optional.of(notificacion));

        when(notificacionRepository.save(any(Notificacion.class)))
                .thenReturn(actualizada);

        Notificacion resultado =
                notificacionService.actualizar(1L, notificacionDTO);

        assertEquals("SMS", resultado.getTipoNotificacion());

        verify(notificacionRepository)
                .save(any(Notificacion.class));
    }

    @Test
    void actualizarDebeLanzarExcepcionSiNoExiste() {

        when(notificacionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> notificacionService.actualizar(99L, notificacionDTO)
        );
    }

    @Test
    void eliminarDebeEliminarNotificacion() {

        when(notificacionRepository.findById(1L))
                .thenReturn(Optional.of(notificacion));

        notificacionService.eliminar(1L);

        verify(notificacionRepository).delete(notificacion);
    }

    @Test
    void eliminarDebeLanzarExcepcionSiNoExiste() {

        when(notificacionRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> notificacionService.eliminar(99L)
        );
    }

    @Test
    void existePorIdDebeRetornarTrue() {

        when(notificacionRepository.existsById(1L))
                .thenReturn(true);

        assertTrue(notificacionService.existePorId(1L));

        verify(notificacionRepository).existsById(1L);
    }

    @Test
    void existePorIdDebeRetornarFalse() {

        when(notificacionRepository.existsById(99L))
                .thenReturn(false);

        assertFalse(notificacionService.existePorId(99L));

        verify(notificacionRepository).existsById(99L);
    }
}