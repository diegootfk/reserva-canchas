package com.reservacanchas.cl.horario_service.service;

import com.reservacanchas.cl.horario_service.dto.HorarioDTO;
import com.reservacanchas.cl.horario_service.exception.BadRequestException;
import com.reservacanchas.cl.horario_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.horario_service.model.Horario;
import com.reservacanchas.cl.horario_service.repository.HorarioRepository;

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
class HorarioServiceTest {

    @Mock
    private HorarioRepository horarioRepository;

    @InjectMocks
    private HorarioService horarioService;

    private Horario horario;
    private HorarioDTO horarioDTO;

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

        horarioDTO = new HorarioDTO(
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

        HorarioDTO dto = new HorarioDTO(
                1L,
                "MARTES",
                "10:00",
                "11:00",
                "ACTIVO"
        );

        Horario horarioActualizado = new Horario(
                1L,
                1L,
                "MARTES",
                "10:00",
                "11:00",
                "ACTIVO"
        );

        when(horarioRepository.findById(1L))
                .thenReturn(Optional.of(horario));

        when(horarioRepository.save(any(Horario.class)))
                .thenReturn(horarioActualizado);

        Horario resultado =
                horarioService.actualizar(1L, dto);

        assertNotNull(resultado);
        assertEquals("MARTES", resultado.getDiaSemana());

        verify(horarioRepository)
                .save(any(Horario.class));
    }

    @Test
    void actualizarDebeLanzarExcepcionSiNoExiste() {

        HorarioDTO dto = new HorarioDTO(
                1L,
                "MARTES",
                "10:00",
                "11:00",
                "ACTIVO"
        );

        when(horarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> horarioService.actualizar(99L, dto)
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
    void eliminarDebeLanzarExcepcionSiNoExiste() {

        when(horarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> horarioService.eliminar(99L)
        );
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

    // -------------------------------------------------------------------
    // REGLAS DE NEGOCIO
    // -------------------------------------------------------------------

    @Test
    @DisplayName("Regla de Negocio: Debe lanzar excepción si el día de la semana está vacío")
    void guardarDebeLanzarExcepcionSiDiaSemanaVacio() {
        // Given
        horarioDTO.setDiaSemana("");

        // When / Then
        assertThrows(
                BadRequestException.class,
                () -> horarioService.guardar(horarioDTO)
        );

        verify(horarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Regla de Negocio: Debe lanzar excepción si el día de la semana es nulo")
    void guardarDebeLanzarExcepcionSiDiaSemanaEsNulo() {
        // Given
        horarioDTO.setDiaSemana(null);

        // When / Then
        assertThrows(
                BadRequestException.class,
                () -> horarioService.guardar(horarioDTO)
        );

        verify(horarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Regla de Negocio: Debe lanzar excepción si horaFin no es posterior a horaInicio")
    void guardarDebeLanzarExcepcionSiHoraFinEsAnteriorAInicio() {
        // Given
        horarioDTO.setHoraInicio("10:00");
        horarioDTO.setHoraFin("09:00");

        // When / Then
        assertThrows(
                BadRequestException.class,
                () -> horarioService.guardar(horarioDTO)
        );

        verify(horarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Regla de Negocio: Debe lanzar excepción si horaFin es igual a horaInicio")
    void guardarDebeLanzarExcepcionSiHoraFinEsIgualAInicio() {
        // Given
        horarioDTO.setHoraInicio("10:00");
        horarioDTO.setHoraFin("10:00");

        // When / Then
        assertThrows(
                BadRequestException.class,
                () -> horarioService.guardar(horarioDTO)
        );

        verify(horarioRepository, never()).save(any());
    }
}