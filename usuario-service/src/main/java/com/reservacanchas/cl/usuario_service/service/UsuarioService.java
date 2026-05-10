package com.reservacanchas.cl.usuario_service.service;

import com.reservacanchas.cl.usuario_service.dto.UsuarioDTO;
import com.reservacanchas.cl.usuario_service.exception.BadRequestException;
import com.reservacanchas.cl.usuario_service.exception.ResourceNotFoundException;
import com.reservacanchas.cl.usuario_service.model.Usuario;
import com.reservacanchas.cl.usuario_service.repository.UsuarioRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario guardar(UsuarioDTO usuarioDTO) {

        if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().isBlank()) {
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

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado"));
    }

    public Usuario actualizar(Long id, UsuarioDTO usuarioDTO) {

        Usuario usuario = buscarPorId(id);

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setEstado(usuarioDTO.getEstado());
        usuario.setIdRol(usuarioDTO.getIdRol());

        return usuarioRepository.save(usuario);
    }

    public void eliminar(Long id) {

        Usuario usuario = buscarPorId(id);

        usuarioRepository.delete(usuario);
    }

    public boolean existePorId(Long id) {
        return usuarioRepository.existsById(id);
    }
}