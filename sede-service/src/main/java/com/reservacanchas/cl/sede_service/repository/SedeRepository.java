package com.reservacanchas.cl.sede_service.repository;

import com.reservacanchas.cl.sede_service.model.Sede;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SedeRepository extends JpaRepository<Sede, Long> {
}