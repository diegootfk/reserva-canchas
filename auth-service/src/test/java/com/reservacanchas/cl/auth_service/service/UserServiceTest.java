package com.reservacanchas.cl.auth_service.service;

import com.reservacanchas.cl.auth_service.model.User;
import com.reservacanchas.cl.auth_service.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private HashService hashService;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {

        user = new User(
                1L,
                "test@test.cl",
                "hashedPassword",
                "USER"
        );
    }


    @Test
    @DisplayName("login: Debe retornar token cuando credenciales son correctas")
    void loginDebeRetornarToken() {
        // Given
        when(userRepository.findByEmail("test@test.cl"))
                .thenReturn(user);

        when(hashService.sha1("1234"))
                .thenReturn("hashedPassword");

        when(jwtService.generateToken("test@test.cl", "USER"))
                .thenReturn("jwt-token");

        // When
        String resultado = userService.login("test@test.cl", "1234");

        // Then
        assertNotNull(resultado);
        assertEquals("jwt-token", resultado);

        verify(jwtService).generateToken("test@test.cl", "USER");
    }

    @Test
    @DisplayName("login: Debe retornar null si el usuario no existe")
    void loginDebeRetornarNullSiUsuarioNoExiste() {
        // Given
        when(userRepository.findByEmail("noexiste@test.cl"))
                .thenReturn(null);

        // When
        String resultado = userService.login("noexiste@test.cl", "1234");

        // Then
        assertNull(resultado);

        verify(jwtService, never()).generateToken(any(), any());
    }

    @Test
    @DisplayName("login: Debe retornar null si la contraseña es incorrecta")
    void loginDebeRetornarNullSiPasswordIncorrecta() {
        // Given
        when(userRepository.findByEmail("test@test.cl"))
                .thenReturn(user);

        when(hashService.sha1("wrongpassword"))
                .thenReturn("wrongHash");

        // When
        String resultado = userService.login("test@test.cl", "wrongpassword");

        // Then
        assertNull(resultado);

        verify(jwtService, never()).generateToken(any(), any());
    }

    // -------------------------------------------------------------------
    // register
    // -------------------------------------------------------------------

    @Test
    @DisplayName("register: Debe registrar correctamente un usuario nuevo")
    void registerDebeRetornarMensajeExitoso() {
        // Given
        when(userRepository.findByEmail("nuevo@test.cl"))
                .thenReturn(null);

        when(hashService.sha1("1234"))
                .thenReturn("hashedPassword");

        // When
        String resultado = userService.register("nuevo@test.cl", "1234");

        // Then
        assertEquals("Usuario creado correctamente", resultado);

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("register: Debe retornar mensaje de error si el usuario ya existe")
    void registerDebeRetornarMensajeDeErrorSiYaExiste() {
        // Given
        when(userRepository.findByEmail("test@test.cl"))
                .thenReturn(user);

        // When
        String resultado = userService.register("test@test.cl", "1234");

        // Then
        assertEquals("Usuario ya existe", resultado);

        verify(userRepository, never()).save(any());
    }

    // -------------------------------------------------------------------
    // registerAdmin
    // -------------------------------------------------------------------

    @Test
    @DisplayName("registerAdmin: Debe registrar correctamente un administrador nuevo")
    void registerAdminDebeRetornarMensajeExitoso() {
        // Given
        when(userRepository.findByEmail("admin@test.cl"))
                .thenReturn(null);

        when(hashService.sha1("adminpass"))
                .thenReturn("hashedAdminPass");

        // When
        String resultado = userService.registerAdmin("admin@test.cl", "adminpass");

        // Then
        assertEquals("Administrador creado correctamente", resultado);

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("registerAdmin: Debe retornar mensaje de error si el admin ya existe")
    void registerAdminDebeRetornarMensajeDeErrorSiYaExiste() {
        // Given
        when(userRepository.findByEmail("test@test.cl"))
                .thenReturn(user);

        // When
        String resultado = userService.registerAdmin("test@test.cl", "adminpass");

        // Then
        assertEquals("Usuario ya existe", resultado);

        verify(userRepository, never()).save(any());
    }


    @Test
    @DisplayName("getRole: Debe retornar el rol del usuario")
    void getRoleDebeRetornarRol() {
        // Given
        when(userRepository.findByEmail("test@test.cl"))
                .thenReturn(user);

        // When
        String rol = userService.getRole("test@test.cl");

        // Then
        assertEquals("USER", rol);
    }

    @Test
    @DisplayName("getRole: Debe retornar null si el usuario no existe")
    void getRoleDebeRetornarNullSiUsuarioNoExiste() {
        // Given
        when(userRepository.findByEmail("noexiste@test.cl"))
                .thenReturn(null);

        // When
        String rol = userService.getRole("noexiste@test.cl");

        // Then
        assertNull(rol);
    }
}
