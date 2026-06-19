package com.reservacanchas.cl.usuario_service.service;

import com.reservacanchas.cl.usuario_service.dto.UsuarioDTO;
import com.reservacanchas.cl.usuario_service.exception.BadRequestException;
import com.reservacanchas.cl.usuario_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.usuario_service.model.Usuario;
import com.reservacanchas.cl.usuario_service.repository.UsuarioRepository;

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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {

        usuario = new Usuario(
                1L,
                "Diego",
                "Perez",
                "diego@test.cl",
                "1234",
                "999999999",
                "ACTIVO",
                1L
        );

        usuarioDTO = new UsuarioDTO(
                "Diego",
                "Perez",
                "diego@test.cl",
                "1234",
                "999999999",
                "ACTIVO",
                1L
        );
    }

    @Test
    void deberiaGuardarUsuario() {

        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuario);

        Usuario resultado = usuarioService.guardar(usuarioDTO);

        assertNotNull(resultado);
        assertEquals("Diego", resultado.getNombre());
        assertEquals("Perez", resultado.getApellido());

        verify(usuarioRepository, times(1))
                .save(any(Usuario.class));
    }

    @Test
    void deberiaLanzarBadRequestCuandoEmailEsVacio() {

        usuarioDTO.setEmail("");

        assertThrows(
                BadRequestException.class,
                () -> usuarioService.guardar(usuarioDTO)
        );

        verify(usuarioRepository, never())
                .save(any());
    }

    @Test
    void deberiaListarUsuarios() {

        when(usuarioRepository.findAll())
                .thenReturn(List.of(usuario));

        List<Usuario> usuarios = usuarioService.listar();

        assertEquals(1, usuarios.size());
        assertEquals("Diego", usuarios.get(0).getNombre());

        verify(usuarioRepository, times(1))
                .findAll();
    }

    @Test
    void deberiaBuscarUsuarioPorId() {

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());

        verify(usuarioRepository, times(1))
                .findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionCuandoUsuarioNoExiste() {

        when(usuarioRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> usuarioService.buscarPorId(99L)
        );

        verify(usuarioRepository, times(1))
                .findById(99L);
    }

    @Test
    void deberiaActualizarUsuario() {

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuario);

        Usuario actualizado =
                usuarioService.actualizar(1L, usuarioDTO);

        assertNotNull(actualizado);
        assertEquals("Diego", actualizado.getNombre());

        verify(usuarioRepository, times(1))
                .save(any(Usuario.class));
    }

    @Test
    void deberiaEliminarUsuario() {

        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(usuario));

        usuarioService.eliminar(1L);

        verify(usuarioRepository, times(1))
                .delete(usuario);
    }

    @Test
    void deberiaVerificarExistenciaUsuario() {

        when(usuarioRepository.existsById(1L))
                .thenReturn(true);

        boolean existe = usuarioService.existePorId(1L);

        assertTrue(existe);

        verify(usuarioRepository, times(1))
                .existsById(1L);
    }

    @Test
    void deberiaRetornarFalseCuandoUsuarioNoExiste() {

        when(usuarioRepository.existsById(99L))
                .thenReturn(false);

        boolean existe = usuarioService.existePorId(99L);

        assertFalse(existe);

        verify(usuarioRepository, times(1))
                .existsById(99L);
    }
}