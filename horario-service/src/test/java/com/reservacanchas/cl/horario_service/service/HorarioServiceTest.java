package com.reservacanchas.cl.horario_service.service;

import com.reservacanchas.cl.horario_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.horario_service.model.Horario;
import com.reservacanchas.cl.horario_service.repository.HorarioRepository;

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
class HorarioServiceTest {

    @Mock
    private HorarioRepository horarioRepository;

    @InjectMocks
    private HorarioService horarioService;

    private Horario horario;

    @BeforeEach
    void setUp() {

        horario = new Horario(
                1L,
                1L,
                "LUNES",
                "09:00",
                "10:00",
                "ACTIVO"
        );
    }

    @Test
    void listarDebeRetornarHorarios() {

        when(horarioRepository.findAll())
                .thenReturn(List.of(horario));

        List<Horario> resultado = horarioService.listar();

        assertEquals(1, resultado.size());

        verify(horarioRepository).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarHorario() {

        when(horarioRepository.findById(1L))
                .thenReturn(Optional.of(horario));

        Horario resultado = horarioService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscarPorIdDebeLanzarExcepcion() {

        when(horarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> horarioService.buscarPorId(99L)
        );
    }

    @Test
    void actualizarDebeActualizarHorario() {

        when(horarioRepository.findById(1L))
                .thenReturn(Optional.of(horario));

        when(horarioRepository.save(any(Horario.class)))
                .thenReturn(horario);

        Horario resultado =
                horarioService.actualizar(1L, horario);

        assertNotNull(resultado);

        verify(horarioRepository).save(any(Horario.class));
    }

    @Test
    void actualizarDebeLanzarExcepcionSiNoExiste() {

        when(horarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> horarioService.actualizar(99L, horario)
        );
    }

    @Test
    void eliminarDebeEliminarHorario() {

        when(horarioRepository.findById(1L))
                .thenReturn(Optional.of(horario));

        horarioService.eliminar(1L);

        verify(horarioRepository).delete(horario);
    }

    @Test
    void existePorIdDebeRetornarTrue() {

        when(horarioRepository.existsById(1L))
                .thenReturn(true);

        assertTrue(horarioService.existePorId(1L));
    }

    @Test
    void existePorIdDebeRetornarFalse() {

        when(horarioRepository.existsById(99L))
                .thenReturn(false);

        assertFalse(horarioService.existePorId(99L));
    }
}