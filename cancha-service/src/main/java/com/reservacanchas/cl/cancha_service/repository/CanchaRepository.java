package com.reservacanchas.cl.cancha_service.repository;

import com.reservacanchas.cl.cancha_service.model.Cancha;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CanchaRepository extends JpaRepository<Cancha, Long> {
}