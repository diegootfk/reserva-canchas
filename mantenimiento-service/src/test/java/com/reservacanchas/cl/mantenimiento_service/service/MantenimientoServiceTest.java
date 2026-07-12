package com.reservacanchas.cl.mantenimiento_service.service;

import com.reservacanchas.cl.mantenimiento_service.dto.MantenimientoDTO;
import com.reservacanchas.cl.mantenimiento_service.exception.BadRequestException;
import com.reservacanchas.cl.mantenimiento_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.mantenimiento_service.model.Mantenimiento;
import com.reservacanchas.cl.mantenimiento_service.repository.MantenimientoRepository;

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
class MantenimientoServiceTest {

    @Mock
    private MantenimientoRepository mantenimientoRepository;

    @InjectMocks
    private MantenimientoService mantenimientoService;

    private Mantenimiento mantenimiento;
    private MantenimientoDTO mantenimientoDTO;

    @BeforeEach
    void setUp() {

        mantenimiento = new Mantenimiento(
                1L,
                1L,
                "2025-06-20",
                "2025-06-21",
                "Cambio de césped",
                "PENDIENTE"
        );

        mantenimientoDTO = new MantenimientoDTO(
                1L,
                "2025-06-20",
                "2025-06-21",
                "Cambio de césped",
                "PENDIENTE"
        );
    }

    @Test
    void listarDebeRetornarMantenimientos() {

        when(mantenimientoRepository.findAll())
                .thenReturn(List.of(mantenimiento));

        List<Mantenimiento> resultado =
                mantenimientoService.listar();

        assertEquals(1, resultado.size());

        verify(mantenimientoRepository).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarMantenimiento() {

        when(mantenimientoRepository.findById(1L))
                .thenReturn(Optional.of(mantenimiento));

        Mantenimiento resultado =
                mantenimientoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(mantenimientoRepository).findById(1L);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcion() {

        when(mantenimientoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> mantenimientoService.buscarPorId(99L)
        );
    }

    @Test
    void actualizarDebeActualizarMantenimiento() {

        MantenimientoDTO dto = new MantenimientoDTO(
                1L,
                "2025-06-25",
                "2025-06-26",
                "Mantención completa",
                "FINALIZADO"
        );

        Mantenimiento actualizado = new Mantenimiento(
                1L,
                1L,
                "2025-06-25",
                "2025-06-26",
                "Mantención completa",
                "FINALIZADO"
        );

        when(mantenimientoRepository.findById(1L))
                .thenReturn(Optional.of(mantenimiento));

        when(mantenimientoRepository.save(any(Mantenimiento.class)))
                .thenReturn(actualizado);

        Mantenimiento resultado =
                mantenimientoService.actualizar(1L, dto);

        assertEquals("FINALIZADO", resultado.getEstado());

        verify(mantenimientoRepository)
                .save(any(Mantenimiento.class));
    }

    @Test
    void actualizarDebeLanzarExcepcionSiNoExiste() {

        when(mantenimientoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> mantenimientoService.actualizar(99L, mantenimientoDTO)
        );
    }

    @Test
    void eliminarDebeEliminarMantenimiento() {

        when(mantenimientoRepository.findById(1L))
                .thenReturn(Optional.of(mantenimiento));

        mantenimientoService.eliminar(1L);

        verify(mantenimientoRepository).delete(mantenimiento);
    }

    @Test
    void eliminarDebeLanzarExcepcionSiNoExiste() {

        when(mantenimientoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> mantenimientoService.eliminar(99L)
        );
    }

    @Test
    void existePorIdDebeRetornarTrue() {

        when(mantenimientoRepository.existsById(1L))
                .thenReturn(true);

        assertTrue(mantenimientoService.existePorId(1L));

        verify(mantenimientoRepository).existsById(1L);
    }

    @Test
    void existePorIdDebeRetornarFalse() {

        when(mantenimientoRepository.existsById(99L))
                .thenReturn(false);

        assertFalse(mantenimientoService.existePorId(99L));

        verify(mantenimientoRepository).existsById(99L);
    }

    // -------------------------------------------------------------------
    // REGLA DE NEGOCIO: descripción no puede estar vacía
    // -------------------------------------------------------------------

    @Test
    @DisplayName("Regla de Negocio: Debe lanzar excepción si la descripción está vacía")
    void guardarDebeLanzarExcepcionSiDescripcionEsVacia() {
        // Given
        mantenimientoDTO.setDescripcion("");

        // When / Then
        assertThrows(
                BadRequestException.class,
                () -> mantenimientoService.guardar(mantenimientoDTO)
        );

        verify(mantenimientoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Regla de Negocio: Debe lanzar excepción si la descripción es nula")
    void guardarDebeLanzarExcepcionSiDescripcionEsNula() {
        // Given
        mantenimientoDTO.setDescripcion(null);

        // When / Then
        assertThrows(
                BadRequestException.class,
                () -> mantenimientoService.guardar(mantenimientoDTO)
        );

        verify(mantenimientoRepository, never()).save(any());
    }
}