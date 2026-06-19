package com.reservacanchas.cl.cancha_service.service;

import com.reservacanchas.cl.cancha_service.dto.CanchaDTO;
import com.reservacanchas.cl.cancha_service.exception.BadRequestException;
import com.reservacanchas.cl.cancha_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.cancha_service.model.Cancha;
import com.reservacanchas.cl.cancha_service.repository.CanchaRepository;

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
class CanchaServiceTest {

    @Mock
    private CanchaRepository canchaRepository;

    @InjectMocks
    private CanchaService canchaService;

    private Cancha cancha;
    private CanchaDTO canchaDTO;

    @BeforeEach
    void setUp() {

        cancha = new Cancha(
                1L,
                "Cancha Principal",
                "Fútbol",
                25000.0,
                22,
                "ACTIVA"
        );

        canchaDTO = new CanchaDTO(
                "Cancha Principal",
                "Fútbol",
                25000.0,
                22,
                "ACTIVA"
        );
    }

    @Test
    void guardarDebeRetornarCanchaGuardada() {

        when(canchaRepository.save(any(Cancha.class))).thenReturn(cancha);

        Cancha resultado = canchaService.guardar(canchaDTO);

        assertNotNull(resultado);
        assertEquals("Cancha Principal", resultado.getNombre());

        verify(canchaRepository).save(any(Cancha.class));
    }

    @Test
    void guardarDebeLanzarExcepcionSiNombreEsVacio() {

        canchaDTO.setNombre("");

        assertThrows(
                BadRequestException.class,
                () -> canchaService.guardar(canchaDTO)
        );

        verify(canchaRepository, never()).save(any());
    }

    @Test
    void listarDebeRetornarListaDeCanchas() {

        when(canchaRepository.findAll())
                .thenReturn(List.of(cancha));

        List<Cancha> resultado = canchaService.listar();

        assertEquals(1, resultado.size());

        verify(canchaRepository).findAll();
    }

    @Test
    void buscarPorIdDebeRetornarCancha() {

        when(canchaRepository.findById(1L))
                .thenReturn(Optional.of(cancha));

        Cancha resultado = canchaService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());

        verify(canchaRepository).findById(1L);
    }

    @Test
    void buscarPorIdDebeLanzarExcepcionSiNoExiste() {

        when(canchaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> canchaService.buscarPorId(99L)
        );
    }

    @Test
    void actualizarDebeModificarCancha() {

        when(canchaRepository.findById(1L))
                .thenReturn(Optional.of(cancha));

        when(canchaRepository.save(any(Cancha.class)))
                .thenReturn(cancha);

        Cancha resultado = canchaService.actualizar(1L, canchaDTO);

        assertNotNull(resultado);

        verify(canchaRepository).save(any(Cancha.class));
    }

    @Test
    void actualizarDebeLanzarExcepcionSiNoExiste() {

        when(canchaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> canchaService.actualizar(99L, canchaDTO)
        );
    }

    @Test
    void eliminarDebeEliminarCancha() {

        when(canchaRepository.findById(1L))
                .thenReturn(Optional.of(cancha));

        canchaService.eliminar(1L);

        verify(canchaRepository).delete(cancha);
    }

    @Test
    void existePorIdDebeRetornarTrue() {

        when(canchaRepository.existsById(1L))
                .thenReturn(true);

        assertTrue(canchaService.existePorId(1L));
    }

    @Test
    void existePorIdDebeRetornarFalse() {

        when(canchaRepository.existsById(99L))
                .thenReturn(false);

        assertFalse(canchaService.existePorId(99L));
    }
}