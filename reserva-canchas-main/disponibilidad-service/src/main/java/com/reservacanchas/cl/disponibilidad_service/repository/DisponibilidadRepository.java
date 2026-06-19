package com.reservacanchas.cl.disponibilidad_service.repository;

import com.reservacanchas.cl.disponibilidad_service.model.Disponibilidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Long> {
}