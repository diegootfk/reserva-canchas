package com.reservacanchas.cl.usuario_service.repository;

import com.reservacanchas.cl.usuario_service.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}