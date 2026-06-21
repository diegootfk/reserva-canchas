package com.reservacanchas.cl.auth_service.service;

import com.reservacanchas.cl.auth_service.model.User;
import com.reservacanchas.cl.auth_service.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final HashService hashService;

    public UserService(UserRepository userRepository, JwtService jwtService, HashService hashService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.hashService = hashService;
    }

    public String login(String email, String password) {

        logger.info("Iniciando proceso de login para email: {}", email);

        User user = userRepository.findByEmail(email);

        if (user == null) {
            logger.warn("Login fallido: no existe usuario con email: {}", email);
            return null;
        }

        logger.debug("Usuario encontrado para email: {}", email);

        String passwordEncriptada = hashService.sha1(password);

        if (!passwordEncriptada.equals(user.getPassword())) {
            logger.warn("Login fallido: contraseña inválida para email: {}", email);
            return null;
        }

        String token = jwtService.generateToken(email, user.getRole());

        logger.info("Login exitoso para email: {} con rol: {}", email, user.getRole());

        return token;
    }

    public String register(String email, String password) {

        logger.info("Iniciando proceso de registro para email: {}", email);

        User existe = userRepository.findByEmail(email);

        if (existe != null) {
            logger.warn("Registro rechazado: usuario ya existe con email: {}", email);
            return "Usuario ya existe";
        }

        User user = new User();

        user.setEmail(email);
        user.setPassword(hashService.sha1(password));
        user.setRole("USER");

        User usuarioGuardado = userRepository.save(user);

        logger.info("Usuario registrado correctamente con ID: {} y email: {}",
                usuarioGuardado.getId(),
                usuarioGuardado.getEmail());

        return "Usuario creado correctamente";
    }

    public String getRole(String email) {

        logger.info("Consultando rol para email: {}", email);

        User user = userRepository.findByEmail(email);

        if (user == null) {
            logger.warn("No se encontró usuario al consultar rol con email: {}", email);
            return null;
        }

        logger.info("Rol encontrado para email {}: {}", email, user.getRole());

        return user.getRole();
    }
}