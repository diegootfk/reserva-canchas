package com.reservacanchas.cl.reserva_service.repository;

import com.reservacanchas.cl.reserva_service.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}