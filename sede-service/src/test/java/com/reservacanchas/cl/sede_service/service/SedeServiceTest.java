package com.reservacanchas.cl.sede_service.service;

import com.reservacanchas.cl.sede_service.exception.BadRequestException;
import com.reservacanchas.cl.sede_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.sede_service.model.Sede;
import com.reservacanchas.cl.sede_service.repository.SedeRepository;

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
class SedeServiceTest {

    @Mock
    private SedeRepository sedeRepository;

    @InjectMocks
    private SedeService sedeService;

    private Sede sede;

    @BeforeEach
    void setUp() {

        sede = new Sede(
                1L,
                "Antonio Varas",
                "Av. Antonio Varas 666",
                "Providencia",
                "22223333",
                "ACTIVA"
        );
    }

    @Test
    void guardarDebeGuardarSede() {

        when(sedeRepository.save(any(Sede.class)))
                .thenReturn(sede);

        Sede resultado = sedeService.guardar(sede);

        assertNotNull(resultado);
        assertEquals("Antonio Varas", resultado.getNombre());

        verify(sedeRepository).save(any(Sede.class));
    }

    @Test
    void guardarDebeLanzarExcepcionSiNombreVacio() {

        sede.setNombre("");

        assertThrows(
                BadRequestException.class,
                () -> sedeService.guardar(sede)
        );
    }

    @Test
    void listarDebeRetornarLista() {

        when(sedeRepository.findAll())
                .thenReturn(List.of(sede));

        List<Sede> resultado = sedeService.listar();

        assertEquals(1, resultado.size());

        verify(sedeRepository).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarSede() {

        when(sedeRepository.findById(1L))
                .thenReturn(Optional.of(sede));

        Sede resultado = sedeService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscarPorIdDebeLanzarExcepcion() {

        when(sedeRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> sedeService.buscarPorId(99L)
        );
    }

    @Test
    void actualizarDebeActualizarSede() {

        when(sedeRepository.findById(1L))
                .thenReturn(Optional.of(sede));

        when(sedeRepository.save(any(Sede.class)))
                .thenReturn(sede);

        Sede resultado =
                sedeService.actualizar(1L, sede);

        assertNotNull(resultado);

        verify(sedeRepository).save(any(Sede.class));
    }

    @Test
    void actualizarDebeLanzarExcepcionSiNoExiste() {

        when(sedeRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> sedeService.actualizar(99L, sede)
        );
    }

    @Test
    void eliminarDebeEliminarSede() {

        when(sedeRepository.findById(1L))
                .thenReturn(Optional.of(sede));

        sedeService.eliminar(1L);

        verify(sedeRepository).delete(sede);
    }

    @Test
    void existePorIdDebeRetornarTrue() {

        when(sedeRepository.existsById(1L))
                .thenReturn(true);

        assertTrue(sedeService.existePorId(1L));
    }

    @Test
    void existePorIdDebeRetornarFalse() {

        when(sedeRepository.existsById(99L))
                .thenReturn(false);

        assertFalse(sedeService.existePorId(99L));
    }
}