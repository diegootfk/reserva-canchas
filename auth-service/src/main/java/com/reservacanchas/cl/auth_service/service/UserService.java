package com.reservacanchas.cl.auth_service.service;

import com.reservacanchas.cl.auth_service.model.User;
import com.reservacanchas.cl.auth_service.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger =
            LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final HashService hashService;

    public UserService(UserRepository userRepository,
                       JwtService jwtService,
                       HashService hashService) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.hashService = hashService;
    }

    public String login(String email, String password) {

        logger.info("Intentando iniciar sesión con email: {}", email);

        User user = userRepository.findByEmail(email);

        if (user == null) {

            logger.warn("Login fallido. Usuario no encontrado: {}", email);

            return null;
        }

        String passwordEncriptada = hashService.sha1(password);

        if (!passwordEncriptada.equals(user.getPassword())) {

            logger.warn("Login fallido. Contraseña incorrecta para usuario: {}", email);

            return null;
        }

        logger.info("Login exitoso para usuario: {}", email);

        return jwtService.generateToken(email, user.getRole());
    }

    public String register(String email, String password) {

        logger.info("Intentando registrar usuario: {}", email);

        User existe = userRepository.findByEmail(email);

        if (existe != null) {

            logger.warn("Registro rechazado. Usuario ya existe: {}", email);

            return "Usuario ya existe";
        }

        User user = new User();

        user.setEmail(email);
        user.setPassword(hashService.sha1(password));
        user.setRole("USER");

        userRepository.save(user);

        logger.info("Usuario registrado correctamente: {}", email);

        return "Usuario creado correctamente";
    }

    public String registerAdmin(String email, String password) {

        logger.info("Intentando registrar administrador: {}", email);

        User existe = userRepository.findByEmail(email);

        if (existe != null) {

            logger.warn("Registro de administrador rechazado. Usuario ya existe: {}", email);

            return "Usuario ya existe";
        }

        User user = new User();

        user.setEmail(email);
        user.setPassword(hashService.sha1(password));
        user.setRole("ADMIN");

        userRepository.save(user);

        logger.info("Administrador registrado correctamente: {}", email);

        return "Administrador creado correctamente";
    }

    public String getRole(String email) {

        logger.info("Consultando rol para email: {}", email);

        User user = userRepository.findByEmail(email);

        if (user == null) {

            logger.warn("No se encontró usuario para email: {}", email);

            return null;
        }

        logger.info("Rol encontrado para {}: {}", email, user.getRole());

        return user.getRole();
    }
}