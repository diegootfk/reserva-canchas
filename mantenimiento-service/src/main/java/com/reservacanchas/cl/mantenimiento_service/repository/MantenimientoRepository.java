package com.reservacanchas.cl.mantenimiento_service.repository;

import com.reservacanchas.cl.mantenimiento_service.model.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {
}