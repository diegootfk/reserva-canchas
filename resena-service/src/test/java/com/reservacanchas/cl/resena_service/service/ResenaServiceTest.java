package com.reservacanchas.cl.resena_service.service;

import com.reservacanchas.cl.resena_service.dto.ResenaDTO;
import com.reservacanchas.cl.resena_service.exception.BadRequestException;
import com.reservacanchas.cl.resena_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.resena_service.model.Resena;
import com.reservacanchas.cl.resena_service.repository.ResenaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class ResenaServiceTest {

    @Mock
    private ResenaRepository resenaRepository;

    @InjectMocks
    private ResenaService resenaService;

    private Resena resena;
    private ResenaDTO resenaDTO;

    @BeforeEach
    void setUp() {

        resena = new Resena(
                1L,
                1L,
                1L,
                1L,
                5,
                "Excelente cancha",
                "2025-06-20"
        );

        resenaDTO = new ResenaDTO(
                1L,
                1L,
                1L,
                5,
                "Excelente cancha",
                "2025-06-20"
        );
    }

    @Test
    void listarDebeRetornarResenas() {

        when(resenaRepository.findAll())
                .thenReturn(List.of(resena));

        List<Resena> resultado = resenaService.listar();

        assertEquals(1, resultado.size());

        verify(resenaRepository).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarResena() {

        when(resenaRepository.findById(1L))
                .thenReturn(Optional.of(resena));

        Resena resultado = resenaService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(5, resultado.getCalificacion());

        verify(resenaRepository).findById(1L);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcion() {

        when(resenaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> resenaService.buscarPorId(99L)
        );
    }

    @Test
    void actualizarDebeActualizarResena() {

        Resena actualizada = new Resena(
                1L, 1L, 1L, 1L, 5, "Excelente", "2025-06-21"
        );

        when(resenaRepository.findById(1L))
                .thenReturn(Optional.of(resena));

        when(resenaRepository.save(any(Resena.class)))
                .thenReturn(actualizada);

        Resena resultado =
                resenaService.actualizar(1L, resenaDTO);

        assertEquals(5, resultado.getCalificacion());

        verify(resenaRepository).save(any(Resena.class));
    }

    @Test
    void actualizarDebeLanzarExcepcionSiNoExiste() {

        when(resenaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> resenaService.actualizar(99L, resenaDTO)
        );
    }

    @Test
    void eliminarDebeEliminarResena() {

        when(resenaRepository.findById(1L))
                .thenReturn(Optional.of(resena));

        resenaService.eliminar(1L);

        verify(resenaRepository).delete(resena);
    }

    @Test
    void eliminarDebeLanzarExcepcionSiNoExiste() {

        when(resenaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> resenaService.eliminar(99L)
        );
    }

    @Test
    void existePorIdDebeRetornarTrue() {

        when(resenaRepository.existsById(1L))
                .thenReturn(true);

        assertTrue(resenaService.existePorId(1L));

        verify(resenaRepository).existsById(1L);
    }

    @Test
    void existePorIdDebeRetornarFalse() {

        when(resenaRepository.existsById(99L))
                .thenReturn(false);

        assertFalse(resenaService.existePorId(99L));

        verify(resenaRepository).existsById(99L);
    }

    // -------------------------------------------------------------------
    // REGLA DE NEGOCIO: calificación debe estar entre 1 y 5
    // -------------------------------------------------------------------

    @Test
    @DisplayName("Regla de Negocio: Debe lanzar excepción si calificación es mayor a 5")
    void guardarDebeLanzarExcepcionSiCalificacionEsMayorACinco() {
        // Given
        resenaDTO.setCalificacion(6);

        // When / Then
        assertThrows(
                BadRequestException.class,
                () -> resenaService.guardar(resenaDTO)
        );

        verify(resenaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Regla de Negocio: Debe lanzar excepción si calificación es menor a 1")
    void guardarDebeLanzarExcepcionSiCalificacionEsMenorAUno() {
        // Given
        resenaDTO.setCalificacion(0);

        // When / Then
        assertThrows(
                BadRequestException.class,
                () -> resenaService.guardar(resenaDTO)
        );

        verify(resenaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Regla de Negocio: Debe lanzar excepción si calificación es nula")
    void guardarDebeLanzarExcepcionSiCalificacionEsNula() {
        // Given
        resenaDTO.setCalificacion(null);

        // When / Then
        assertThrows(
                BadRequestException.class,
                () -> resenaService.guardar(resenaDTO)
        );

        verify(resenaRepository, never()).save(any());
    }
}