package com.reservacanchas.cl.disponibilidad_service.service;

import com.reservacanchas.cl.disponibilidad_service.model.Disponibilidad;
import com.reservacanchas.cl.disponibilidad_service.repository.DisponibilidadRepository;

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

    @Test
    void debeListarDisponibilidades() {

        Disponibilidad disponibilidad = new Disponibilidad(
                1L,1L,"2025-06-20","09:00","10:00","DISPONIBLE"
        );

        when(disponibilidadRepository.findAll())
                .thenReturn(List.of(disponibilidad));

        List<Disponibilidad> resultado =
                disponibilidadService.listar();

        assertEquals(1, resultado.size());

        verify(disponibilidadRepository)
                .findAll();
    }

    @Test
    void debeBuscarDisponibilidadPorId() {

        Disponibilidad disponibilidad = new Disponibilidad(
                1L,1L,"2025-06-20","09:00","10:00","DISPONIBLE"
        );

        when(disponibilidadRepository.findById(1L))
                .thenReturn(Optional.of(disponibilidad));

        Disponibilidad resultado =
                disponibilidadService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(disponibilidadRepository)
                .findById(1L);
    }

    @Test
    void debeActualizarDisponibilidad() {

        Disponibilidad actual = new Disponibilidad(
                1L,1L,"2025-06-20","09:00","10:00","DISPONIBLE"
        );

        Disponibilidad nueva = new Disponibilidad(
                1L,1L,"2025-06-21","10:00","11:00","OCUPADO"
        );

        when(disponibilidadRepository.findById(1L))
                .thenReturn(Optional.of(actual));

        when(disponibilidadRepository.save(any()))
                .thenReturn(nueva);

        Disponibilidad resultado =
                disponibilidadService.actualizar(1L,nueva);

        assertEquals("OCUPADO",
                resultado.getEstado());

        verify(disponibilidadRepository)
                .save(any());
    }

    @Test
    void debeEliminarDisponibilidad() {

        Disponibilidad disponibilidad = new Disponibilidad(
                1L,1L,"2025-06-20","09:00","10:00","DISPONIBLE"
        );

        when(disponibilidadRepository.findById(1L))
                .thenReturn(Optional.of(disponibilidad));

        disponibilidadService.eliminar(1L);

        verify(disponibilidadRepository)
                .delete(disponibilidad);
    }

    @Test
    void debeVerificarExistencia() {

        when(disponibilidadRepository.existsById(1L))
                .thenReturn(true);

        boolean existe =
                disponibilidadService.existePorId(1L);

        assertTrue(existe);

        verify(disponibilidadRepository)
                .existsById(1L);
    }
}