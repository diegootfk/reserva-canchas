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

    private static final Logger logger =
            LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario guardar(UsuarioDTO usuarioDTO) {

        logger.info("Intentando guardar usuario con email: {}",
                usuarioDTO.getEmail());

        if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().isBlank()) {

            logger.error("Error al guardar usuario: email vacío");

            throw new BadRequestException("El email es obligatorio");
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setEstado(usuarioDTO.getEstado());
        usuario.setIdRol(usuarioDTO.getIdRol());

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        logger.info("Usuario creado correctamente con ID: {}",
                usuarioGuardado.getId());

        return usuarioGuardado;
    }

    public List<Usuario> listar() {

        logger.info("Listando todos los usuarios");

        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {

        logger.info("Buscando usuario con ID: {}", id);

        return usuarioRepository.findById(id)
                .orElseThrow(() -> {

                    logger.error("Usuario no encontrado con ID: {}", id);

                    return new ResourceNotFoundException(
                            "Usuario no encontrado");
                });
    }

    public Usuario actualizar(Long id, UsuarioDTO usuarioDTO) {

        logger.info("Actualizando usuario con ID: {}", id);

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

        logger.info("Eliminando usuario con ID: {}", id);

        Usuario usuario = buscarPorId(id);

        usuarioRepository.delete(usuario);

        logger.info("Usuario eliminado correctamente con ID: {}", id);
    }

    public boolean existePorId(Long id) {

        logger.info("Verificando existencia de usuario con ID: {}", id);

        return usuarioRepository.existsById(id);
    }
}