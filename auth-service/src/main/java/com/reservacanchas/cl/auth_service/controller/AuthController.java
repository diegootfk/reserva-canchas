package com.reservacanchas.cl.auth_service.controller;

import com.reservacanchas.cl.auth_service.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

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

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        String resultado = userService.register(email, password);

        Map<String, String> response = new HashMap<>();
        response.put("message", resultado);

        return response;
    }
}