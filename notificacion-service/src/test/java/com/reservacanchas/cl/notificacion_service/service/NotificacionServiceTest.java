package com.reservacanchas.cl.notificacion_service.service;

import com.reservacanchas.cl.notificacion_service.model.Notificacion;
import com.reservacanchas.cl.notificacion_service.repository.NotificacionRepository;

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

    @Test
    void debeListarNotificaciones() {

        Notificacion notificacion = new Notificacion(
                1L,
                1L,
                1L,
                "Reserva confirmada",
                "EMAIL",
                "2025-06-20",
                "ENVIADA"
        );

        when(notificacionRepository.findAll())
                .thenReturn(List.of(notificacion));

        List<Notificacion> resultado =
                notificacionService.listar();

        assertEquals(1, resultado.size());

        verify(notificacionRepository).findAll();
    }

    @Test
    void debeBuscarPorId() {

        Notificacion notificacion = new Notificacion(
                1L,
                1L,
                1L,
                "Reserva confirmada",
                "EMAIL",
                "2025-06-20",
                "ENVIADA"
        );

        when(notificacionRepository.findById(1L))
                .thenReturn(Optional.of(notificacion));

        Notificacion resultado =
                notificacionService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(notificacionRepository).findById(1L);
    }

    @Test
    void debeActualizarNotificacion() {

        Notificacion actual = new Notificacion(
                1L,1L,1L,
                "Reserva confirmada",
                "EMAIL",
                "2025-06-20",
                "ENVIADA"
        );

        Notificacion nueva = new Notificacion(
                1L,1L,1L,
                "Reserva modificada",
                "SMS",
                "2025-06-21",
                "PENDIENTE"
        );

        when(notificacionRepository.findById(1L))
                .thenReturn(Optional.of(actual));

        when(notificacionRepository.save(any()))
                .thenReturn(nueva);

        Notificacion resultado =
                notificacionService.actualizar(1L, nueva);

        assertEquals("SMS",
                resultado.getTipoNotificacion());

        verify(notificacionRepository).save(any());
    }

    @Test
    void debeEliminarNotificacion() {

        Notificacion notificacion = new Notificacion(
                1L,1L,1L,
                "Reserva confirmada",
                "EMAIL",
                "2025-06-20",
                "ENVIADA"
        );

        when(notificacionRepository.findById(1L))
                .thenReturn(Optional.of(notificacion));

        notificacionService.eliminar(1L);

        verify(notificacionRepository)
                .delete(notificacion);
    }

    @Test
    void debeVerificarExistencia() {

        when(notificacionRepository.existsById(1L))
                .thenReturn(true);

        boolean existe =
                notificacionService.existePorId(1L);

        assertTrue(existe);

        verify(notificacionRepository)
                .existsById(1L);
    }
}