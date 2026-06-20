package com.reservacanchas.cl.resena_service.service;

import com.reservacanchas.cl.resena_service.model.Resena;
import com.reservacanchas.cl.resena_service.repository.ResenaRepository;

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

    @Test
    void debeListarResenas() {

        Resena resena = new Resena(
                1L,
                1L,
                1L,
                1L,
                5,
                "Excelente cancha",
                "2025-06-20"
        );

        when(resenaRepository.findAll())
                .thenReturn(List.of(resena));

        List<Resena> resultado = resenaService.listar();

        assertEquals(1, resultado.size());

        verify(resenaRepository).findAll();
    }

    @Test
    void debeBuscarResenaPorId() {

        Resena resena = new Resena(
                1L,
                1L,
                1L,
                1L,
                5,
                "Excelente cancha",
                "2025-06-20"
        );

        when(resenaRepository.findById(1L))
                .thenReturn(Optional.of(resena));

        Resena resultado = resenaService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(5, resultado.getCalificacion());

        verify(resenaRepository).findById(1L);
    }

    @Test
    void debeActualizarResena() {

        Resena actual = new Resena(
                1L,1L,1L,1L,
                4,
                "Buena",
                "2025-06-20"
        );

        Resena nueva = new Resena(
                1L,1L,1L,1L,
                5,
                "Excelente",
                "2025-06-21"
        );

        when(resenaRepository.findById(1L))
                .thenReturn(Optional.of(actual));

        when(resenaRepository.save(any()))
                .thenReturn(nueva);

        Resena resultado =
                resenaService.actualizar(1L, nueva);

        assertEquals(5,
                resultado.getCalificacion());

        verify(resenaRepository).save(any());
    }

    @Test
    void debeEliminarResena() {

        Resena resena = new Resena(
                1L,1L,1L,1L,
                5,
                "Excelente",
                "2025-06-20"
        );

        when(resenaRepository.findById(1L))
                .thenReturn(Optional.of(resena));

        resenaService.eliminar(1L);

        verify(resenaRepository)
                .delete(resena);
    }

    @Test
    void debeVerificarExistencia() {

        when(resenaRepository.existsById(1L))
                .thenReturn(true);

        boolean existe =
                resenaService.existePorId(1L);

        assertTrue(existe);

        verify(resenaRepository)
                .existsById(1L);
    }
}