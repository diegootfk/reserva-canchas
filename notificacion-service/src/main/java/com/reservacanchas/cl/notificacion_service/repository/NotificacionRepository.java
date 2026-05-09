package com.reservacanchas.cl.notificacion_service.repository;

import com.reservacanchas.cl.notificacion_service.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
}