package com.reservacanchas.cl.auth_service.controller;

import com.reservacanchas.cl.auth_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(
        name = "Autenticación",
        description = "Endpoints para registro e inicio de sesión utilizando JWT"
)
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Iniciar sesión",
            description = "Permite autenticar un usuario y obtener un token JWT"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado correctamente"),
            @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    })
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String password = request.get("password");

        String token = userService.login(email, password);

        Map<String, String> response = new HashMap<>();

        if (token == null) {
            response.put("status", "error");
            response.put("token", "");
        } else {
            response.put("status", "ok");
            response.put("token", token);
        }

        return response;
    }

    @Operation(
            summary = "Registrar usuario",
            description = "Permite registrar un nuevo usuario con rol USER"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String password = request.get("password");

        String resultado = userService.register(email, password);

        Map<String, String> response = new HashMap<>();
        response.put("message", resultado);

        return response;
    }

    @Operation(
            summary = "Registrar administrador",
            description = "Permite registrar un nuevo usuario con rol ADMIN"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Administrador registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/register-admin")
    public Map<String, String> registerAdmin(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String password = request.get("password");

        String resultado = userService.registerAdmin(email, password);

        Map<String, String> response = new HashMap<>();
        response.put("message", resultado);

        return response;
    }
}