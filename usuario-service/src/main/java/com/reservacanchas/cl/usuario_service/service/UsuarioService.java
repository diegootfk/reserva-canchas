package com.reservacanchas.cl.usuario_service.service;

import com.reservacanchas.cl.usuario_service.dto.UsuarioDTO;
import com.reservacanchas.cl.usuario_service.exception.BadRequestException;
import com.reservacanchas.cl.usuario_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.usuario_service.model.Usuario;
import com.reservacanchas.cl.usuario_service.repository.UsuarioRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario guardar(UsuarioDTO usuarioDTO) {

        logger.info("Iniciando proceso para guardar un nuevo usuario con email: {}", usuarioDTO.getEmail());

        validarEmail(usuarioDTO.getEmail());

        Usuario usuario = new Usuario();

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setEstado(usuarioDTO.getEstado());
        usuario.setIdRol(usuarioDTO.getIdRol());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        logger.info("Usuario creado correctamente con ID: {}", usuarioGuardado.getId());

        return usuarioGuardado;
    }

    public List<Usuario> listar() {

        logger.info("Iniciando búsqueda de todos los usuarios");

        List<Usuario> usuarios = usuarioRepository.findAll();

        logger.info("Búsqueda finalizada. Total de usuarios encontrados: {}", usuarios.size());

        return usuarios;
    }

    public Usuario buscarPorId(Long id) {

        logger.info("Iniciando búsqueda de usuario con ID: {}", id);

        return usuarioRepository.findById(id)
                .map(usuario -> {
                    logger.info("Usuario encontrado correctamente con ID: {}", id);
                    return usuario;
                })
                .orElseThrow(() -> {
                    logger.warn("Usuario no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Usuario no encontrado");
                });
    }

    public Usuario actualizar(Long id, UsuarioDTO usuarioDTO) {

        logger.info("Iniciando actualización de usuario con ID: {}", id);

        validarEmail(usuarioDTO.getEmail());

        Usuario usuario = buscarPorId(id);

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setEstado(usuarioDTO.getEstado());
        usuario.setIdRol(usuarioDTO.getIdRol());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);

        logger.info("Usuario actualizado correctamente con ID: {}", id);

        return usuarioActualizado;
    }

    public void eliminar(Long id) {

        logger.warn("Iniciando eliminación de usuario con ID: {}", id);

        Usuario usuario = buscarPorId(id);

        usuarioRepository.delete(usuario);

        logger.info("Usuario eliminado correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de usuario con ID: {}", id);

        boolean existe = usuarioRepository.existsById(id);

        logger.info("Resultado de existencia para usuario con ID {}: {}", id, existe);

        return existe;
    }

    private void validarEmail(String email) {

        logger.debug("Validando email de usuario");

        if (email == null || email.isBlank()) {
            logger.warn("Validación fallida: el email del usuario está vacío o es nulo");
            throw new BadRequestException("El email es obligatorio");
        }

        logger.debug("Email validado correctamente");
    }
}