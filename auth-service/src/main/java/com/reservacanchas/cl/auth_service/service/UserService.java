package com.reservacanchas.cl.auth_service.service;

import com.reservacanchas.cl.auth_service.model.User;
import com.reservacanchas.cl.auth_service.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final HashService hashService;

    public UserService(UserRepository userRepository, JwtService jwtService, HashService hashService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.hashService = hashService;
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return null;
        }

        String passwordEncriptada = hashService.sha1(password);

        if (!passwordEncriptada.equals(user.getPassword())) {
            return null;
        }

        return jwtService.generateToken(email, user.getRole());
    }

    public String register(String email, String password) {
        User existe = userRepository.findByEmail(email);

        if (existe != null) {
            return "Usuario ya existe";
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashService.sha1(password));
        user.setRole("USER");

        userRepository.save(user);

        return "Usuario creado correctamente";
    }

    public String getRole(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            return null;
        }

        return user.getRole();
    }
}