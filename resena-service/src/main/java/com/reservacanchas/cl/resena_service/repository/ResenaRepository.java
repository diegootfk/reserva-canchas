package com.reservacanchas.cl.resena_service.repository;

import com.reservacanchas.cl.resena_service.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
}