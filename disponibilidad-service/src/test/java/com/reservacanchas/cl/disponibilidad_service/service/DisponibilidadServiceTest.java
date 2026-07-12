package com.reservacanchas.cl.disponibilidad_service.service;

import com.reservacanchas.cl.disponibilidad_service.dto.DisponibilidadDTO;
import com.reservacanchas.cl.disponibilidad_service.exception.BadRequestException;
import com.reservacanchas.cl.disponibilidad_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.disponibilidad_service.model.Disponibilidad;
import com.reservacanchas.cl.disponibilidad_service.repository.DisponibilidadRepository;

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
class DisponibilidadServiceTest {

    @Mock
    private DisponibilidadRepository disponibilidadRepository;

    @InjectMocks
    private DisponibilidadService disponibilidadService;

    private Disponibilidad disponibilidad;
    private DisponibilidadDTO disponibilidadDTO;

    @BeforeEach
    void setUp() {

        disponibilidad = new Disponibilidad(
                1L, 1L, "2025-06-20", "09:00", "10:00", "DISPONIBLE"
        );

        disponibilidadDTO = new DisponibilidadDTO(
                1L,
                "2025-06-20",
                "09:00",
                "10:00",
                "DISPONIBLE"
        );
    }

    @Test
    void listarDebeRetornarDisponibilidades() {

        when(disponibilidadRepository.findAll())
                .thenReturn(List.of(disponibilidad));

        List<Disponibilidad> resultado =
                disponibilidadService.listar();

        assertEquals(1, resultado.size());

        verify(disponibilidadRepository).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarDisponibilidad() {

        when(disponibilidadRepository.findById(1L))
                .thenReturn(Optional.of(disponibilidad));

        Disponibilidad resultado =
                disponibilidadService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(disponibilidadRepository).findById(1L);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcion() {

        when(disponibilidadRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> disponibilidadService.buscarPorId(99L)
        );
    }

    @Test
    void actualizarDebeActualizarDisponibilidad() {

        DisponibilidadDTO dto = new DisponibilidadDTO(
                1L,
                "2025-06-21",
                "10:00",
                "11:00",
                "OCUPADO"
        );

        Disponibilidad actualizada = new Disponibilidad(
                1L, 1L, "2025-06-21", "10:00", "11:00", "OCUPADO"
        );

        when(disponibilidadRepository.findById(1L))
                .thenReturn(Optional.of(disponibilidad));

        when(disponibilidadRepository.save(any(Disponibilidad.class)))
                .thenReturn(actualizada);

        Disponibilidad resultado =
                disponibilidadService.actualizar(1L, dto);

        assertEquals("OCUPADO", resultado.getEstado());

        verify(disponibilidadRepository)
                .save(any(Disponibilidad.class));
    }

    @Test
    void actualizarDebeLanzarExcepcionSiNoExiste() {

        when(disponibilidadRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> disponibilidadService.actualizar(99L, disponibilidadDTO)
        );
    }

    @Test
    void eliminarDebeEliminarDisponibilidad() {

        when(disponibilidadRepository.findById(1L))
                .thenReturn(Optional.of(disponibilidad));

        disponibilidadService.eliminar(1L);

        verify(disponibilidadRepository).delete(disponibilidad);
    }

    @Test
    void eliminarDebeLanzarExcepcionSiNoExiste() {

        when(disponibilidadRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> disponibilidadService.eliminar(99L)
        );
    }

    @Test
    void existePorIdDebeRetornarTrue() {

        when(disponibilidadRepository.existsById(1L))
                .thenReturn(true);

        assertTrue(disponibilidadService.existePorId(1L));

        verify(disponibilidadRepository).existsById(1L);
    }

    @Test
    void existePorIdDebeRetornarFalse() {

        when(disponibilidadRepository.existsById(99L))
                .thenReturn(false);

        assertFalse(disponibilidadService.existePorId(99L));

        verify(disponibilidadRepository).existsById(99L);
    }

    @Test
    void guardarDebeLanzarExcepcionSiFechaEsVacia() {
        // Given
        disponibilidadDTO.setFecha("");

        // When / Then
        assertThrows(
                BadRequestException.class,
                () -> disponibilidadService.guardar(disponibilidadDTO)
        );

        verify(disponibilidadRepository, never()).save(any());
    }

    @Test
    void guardarDebeLanzarExcepcionSiFechaEsNula() {
        // Given
        disponibilidadDTO.setFecha(null);

        // When / Then
        assertThrows(
                BadRequestException.class,
                () -> disponibilidadService.guardar(disponibilidadDTO)
        );

        verify(disponibilidadRepository, never()).save(any());
    }
}