package com.reservacanchas.cl.mantenimiento_service.service;

import com.reservacanchas.cl.mantenimiento_service.dto.MantenimientoDTO;
import com.reservacanchas.cl.mantenimiento_service.model.Mantenimiento;
import com.reservacanchas.cl.mantenimiento_service.repository.MantenimientoRepository;

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

    @Test
    void debeListarMantenimientos() {

        Mantenimiento mantenimiento = new Mantenimiento(
                1L,
                1L,
                "2025-06-20",
                "2025-06-21",
                "Cambio de césped",
                "PENDIENTE"
        );

        when(mantenimientoRepository.findAll())
                .thenReturn(List.of(mantenimiento));

        List<Mantenimiento> resultado =
                mantenimientoService.listar();

        assertEquals(1, resultado.size());

        verify(mantenimientoRepository).findAll();
    }

    @Test
    void debeBuscarPorId() {

        Mantenimiento mantenimiento = new Mantenimiento(
                1L,
                1L,
                "2025-06-20",
                "2025-06-21",
                "Cambio de césped",
                "PENDIENTE"
        );

        when(mantenimientoRepository.findById(1L))
                .thenReturn(Optional.of(mantenimiento));

        Mantenimiento resultado =
                mantenimientoService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(mantenimientoRepository).findById(1L);
    }

    @Test
    void debeActualizarMantenimiento() {

        Mantenimiento actual = new Mantenimiento(
                1L,
                1L,
                "2025-06-20",
                "2025-06-21",
                "Cambio de césped",
                "PENDIENTE"
        );

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
                .thenReturn(Optional.of(actual));

        when(mantenimientoRepository.save(any(Mantenimiento.class)))
                .thenReturn(actualizado);

        Mantenimiento resultado =
                mantenimientoService.actualizar(1L, dto);

        assertEquals("FINALIZADO",
                resultado.getEstado());

        verify(mantenimientoRepository)
                .save(any(Mantenimiento.class));
    }

    @Test
    void debeEliminarMantenimiento() {

        Mantenimiento mantenimiento = new Mantenimiento(
                1L,
                1L,
                "2025-06-20",
                "2025-06-21",
                "Cambio de césped",
                "PENDIENTE"
        );

        when(mantenimientoRepository.findById(1L))
                .thenReturn(Optional.of(mantenimiento));

        mantenimientoService.eliminar(1L);

        verify(mantenimientoRepository)
                .delete(mantenimiento);
    }

    @Test
    void debeVerificarExistencia() {

        when(mantenimientoRepository.existsById(1L))
                .thenReturn(true);

        boolean existe =
                mantenimientoService.existePorId(1L);

        assertTrue(existe);

        verify(mantenimientoRepository)
                .existsById(1L);
    }
}